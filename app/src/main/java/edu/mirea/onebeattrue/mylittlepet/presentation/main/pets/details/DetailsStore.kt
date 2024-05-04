package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Event
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalData
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Note
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.EditPetUseCase
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.DetailsStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.DetailsStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.DetailsStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

internal interface DetailsStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object ClickBack : Intent
        data class SetAge(val age: Int) : Intent
        data class SetWeight(val weight: Int) : Intent
        data class AddEvent(val event: Event) : Intent
        data class AddNote(val note: Note) : Intent
        data class AddMedicalData(val medicalData: MedicalData) : Intent
    }

    data class State(
        val age: Int?,
        val weight: Int?,

        val eventList: List<Event>,
        val noteList: List<Note>,
        val medicalDataList: List<MedicalData>,
    )

    sealed interface Label {
        data object ClickBack : Label
    }
}

internal class DetailsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val editPetUseCase: EditPetUseCase
) {

    fun create(pet: Pet): DetailsStore =
        object : DetailsStore, Store<Intent, State, Label> by storeFactory.create(
            name = "DetailsStore",
            initialState = State(
                age = pet.age,
                weight = pet.weight,

                eventList = pet.eventList,
                noteList = pet.noteList,
                medicalDataList = pet.medicalDataList

            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = { ExecutorImpl(pet) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {
        data class SetAge(val age: Int) : Msg
        data class SetWeight(val weight: Int) : Msg

        data class UpdateEvents(val events: List<Event>) : Msg
        data class UpdateNotes(val notes: List<Note>) : Msg
        data class UpdateMedicalDatas(val medicalDatas: List<MedicalData>) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl(
        private val pet: Pet
    ) : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.SetAge -> {
                    dispatch(Msg.SetAge(intent.age))
                }

                is Intent.SetWeight -> {
                    dispatch(Msg.SetWeight(intent.weight))
                }

                is Intent.AddEvent -> {
                    scope.launch {
                        val oldEventList = getState().eventList
                        val newEventList = oldEventList
                            .toMutableList()
                            .apply { add(intent.event) }
                            .toList()
                        editPetUseCase(pet.copy(eventList = newEventList))
                        dispatch(Msg.UpdateEvents(newEventList))
                    }
                }

                is Intent.AddMedicalData -> {
                    scope.launch {
                        val oldMedicalDataList = getState().medicalDataList
                        val newMedicalDataList = oldMedicalDataList
                            .toMutableList()
                            .apply { add(intent.medicalData) }
                            .toList()
                        editPetUseCase(pet.copy(medicalDataList = newMedicalDataList))
                        dispatch(Msg.UpdateMedicalDatas(newMedicalDataList))
                    }
                }

                is Intent.AddNote -> {
                    scope.launch {
                        val oldNoteList = getState().noteList
                        val newNoteList = oldNoteList
                            .toMutableList()
                            .apply { add(intent.note) }
                            .toList()
                        editPetUseCase(pet.copy(noteList = newNoteList))
                        dispatch(Msg.UpdateNotes(newNoteList))
                    }
                }

                Intent.ClickBack -> {
                    publish(Label.ClickBack)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.SetAge -> copy(age = msg.age)
                is Msg.SetWeight -> copy(weight = msg.weight)

                is Msg.UpdateEvents -> copy(eventList = msg.events)
                is Msg.UpdateMedicalDatas -> copy(medicalDataList = msg.medicalDatas)
                is Msg.UpdateNotes -> copy(noteList = msg.notes)
            }
    }
}
