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
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.notelist.NoteListStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.notelist.NoteListStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.notelist.NoteListStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface NoteListStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object AddNote : Intent
        data class DeleteNote(val note: Note) : Intent
        data object OnClickBack : Intent
    }

    data class State(
        val notes: List<Note>
    )

    sealed interface Label {
        data class OnAddNoteClick(val pet: Pet) : Label
        data object OnClickBack : Label
    }
}

class EventListStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getNoteListUseCase: GetNoteListUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) {
    fun create(
        pet: Pet
    ): NoteListStore =
        object : NoteListStore, Store<Intent, State, Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = State(
                notes = pet.noteList
            ),
            bootstrapper = BootstrapperImpl(pet),
            executorFactory = {
                ExecutorImpl(pet)
            },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class UpdateList(val notes: List<Note>) : Action
    }

    private sealed interface Msg {
        data class UpdateList(val notes: List<Note>) : Msg
    }

    private inner class BootstrapperImpl(
        private val pet: Pet
    ) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getNoteListUseCase(pet.id).collect { updatedList ->
                    dispatch(Action.UpdateList(updatedList))
                }
            }
        }
    }

    private inner class ExecutorImpl(
        private val pet: Pet
    ) : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.UpdateList -> {
                    dispatch(Msg.UpdateList(action.notes))
                }
            }
        }

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.AddNote -> {
                    publish(Label.OnAddNoteClick(pet))
                }

                is Intent.DeleteNote -> {
                    scope.launch {
                        deleteNoteUseCase(intent.note)
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
            }
    }

    companion object {
        private const val STORE_NAME = "EventListStore"
    }
}
