package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.notelist

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Note
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.EditPetUseCase
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.GetPetByIdUseCase
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
        val pet: Pet
    )

    sealed interface Label {
        data class OnAddNoteClick(val pet: Pet) : Label
        data object OnClickBack : Label
    }
}

class EventListStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val editPetUseCase: EditPetUseCase,
    private val getPetByIdUseCase: GetPetByIdUseCase
) {
    fun create(
        pet: Pet
    ): NoteListStore =
        object : NoteListStore, Store<Intent, State, Label> by storeFactory.create(
            name = "EventListStore",
            initialState = State(
                pet = pet
            ),
            bootstrapper = BootstrapperImpl(pet),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class UpdatePet(val pet: Pet) : Action
    }

    private sealed interface Msg {
        data class UpdatePet(val pet: Pet) : Msg
    }

    private inner class BootstrapperImpl(
        private val pet: Pet
    ) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getPetByIdUseCase(pet.id).collect { updatedPet ->
                    dispatch(Action.UpdatePet(updatedPet))
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.UpdatePet -> {
                    dispatch(Msg.UpdatePet(action.pet))
                }
            }
        }

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.AddNote -> {
                    val pet = getState().pet
                    publish(Label.OnAddNoteClick(pet))
                }

                is Intent.DeleteNote -> {
                    scope.launch {
                        val pet = getState().pet

                        val oldNoteList = getState().pet.noteList
                        val newNoteList = oldNoteList
                            .toMutableList()
                            .apply {
                                removeIf {
                                    it.id == intent.note.id
                                }
                            }
                            .toList()

                        editPetUseCase(pet.copy(noteList = newNoteList))
                    }
                }

                Intent.OnClickBack -> publish(Label.OnClickBack)
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.UpdatePet -> {
                    copy(pet = msg.pet)
                }
            }
    }
}
