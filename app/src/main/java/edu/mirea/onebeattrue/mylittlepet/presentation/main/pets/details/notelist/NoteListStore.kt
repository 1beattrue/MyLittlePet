package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.notelist

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Note
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.DeleteNoteUseCase
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.GetNoteListUseCase
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.SynchronizeNotesWithServerUseCase
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.notelist.NoteListStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.notelist.NoteListStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.notelist.NoteListStore.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface NoteListStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object AddNote : Intent
        data class DeleteNote(val note: Note) : Intent
        data object OnClickBack : Intent
        data object Synchronize : Intent
    }

    data class State(
        val notes: List<Note>,
        val isLoading: Boolean,
        val syncError: Boolean,
        val deleteNoteErrorId: Int?,
        val nowDeletingId: Int?,
    )

    sealed interface Label {
        data class OnAddNoteClick(val pet: Pet) : Label
        data object OnClickBack : Label
    }
}

class EventListStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getNoteListUseCase: GetNoteListUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val synchronizeNotesWithServerUseCase: SynchronizeNotesWithServerUseCase
) {
    fun create(
        pet: Pet
    ): NoteListStore =
        object : NoteListStore, Store<Intent, State, Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = State(
                notes = pet.noteList,
                isLoading = false,
                syncError = false,
                deleteNoteErrorId = null,
                nowDeletingId = null,
            ),
            bootstrapper = BootstrapperImpl(pet),
            executorFactory = {
                ExecutorImpl(pet)
            },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data object Loading : Action
        data class UpdateList(val notes: List<Note>) : Action
        data class SyncResult(val isError: Boolean) : Action
    }

    private sealed interface Msg {
        data object Loading : Msg
        data class UpdateList(val notes: List<Note>) : Msg
        data class SyncResult(val isError: Boolean) : Msg
        data class DeleteNoteError(val noteId: Int) : Msg
        data class DeletingNote(val id: Int) : Msg
    }

    private inner class BootstrapperImpl(
        private val pet: Pet
    ) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                dispatch(Action.Loading)
                try {
                    synchronize(
                        petId = pet.id
                    )
                    dispatch(Action.SyncResult(isError = false))
                } catch (_: Exception) {
                    dispatch(Action.SyncResult(isError = true))
                } finally {
                    getNoteListUseCase(pet.id).collect { updatedList ->
                        dispatch(Action.UpdateList(updatedList))
                    }
                }
            }
        }
    }

    private inner class ExecutorImpl(
        private val pet: Pet
    ) : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        private var deletingJob: Job? = null

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.UpdateList -> {
                    dispatch(Msg.UpdateList(action.notes))
                }

                Action.Loading -> {
                    dispatch(Msg.Loading)
                }

                is Action.SyncResult -> {
                    dispatch(Msg.SyncResult(action.isError))
                }
            }
        }

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.AddNote -> {
                    publish(Label.OnAddNoteClick(pet))
                }

                is Intent.DeleteNote -> {
                    deletingJob?.cancel()
                    deletingJob = scope.launch {
                        dispatch(Msg.DeletingNote(intent.note.id))
                        try {
                            withContext(Dispatchers.IO) {
                                deleteNoteUseCase(
                                    note = intent.note
                                )
                            }
                        } catch (_: Exception) {
                            dispatch(Msg.DeleteNoteError(intent.note.id))
                        }
                    }
                }

                Intent.Synchronize -> {
                    scope.launch {
                        dispatch(Msg.Loading)
                        try {
                            synchronize(
                                petId = pet.id
                            )
                            dispatch(Msg.SyncResult(isError = false))
                        } catch (_: Exception) {
                            dispatch(Msg.SyncResult(isError = true))
                        }
                    }
                }

                Intent.OnClickBack -> publish(Label.OnClickBack)
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.UpdateList -> {
                    copy(notes = msg.notes)
                }

                is Msg.DeleteNoteError -> copy(
                    deleteNoteErrorId = msg.noteId,
                    nowDeletingId = null
                )

                is Msg.DeletingNote -> copy(
                    nowDeletingId = msg.id,
                    deleteNoteErrorId = null
                )

                Msg.Loading -> copy(
                    isLoading = true,
                    deleteNoteErrorId = null
                )

                is Msg.SyncResult -> copy(
                    syncError = msg.isError,
                    isLoading = false,
                    deleteNoteErrorId = null
                )
            }
    }

    private suspend fun synchronize(petId: Int) {
        withContext(Dispatchers.IO) {
            synchronizeNotesWithServerUseCase(petId)
        }
    }

    companion object {
        private const val STORE_NAME = "EventListStore"
    }
}
