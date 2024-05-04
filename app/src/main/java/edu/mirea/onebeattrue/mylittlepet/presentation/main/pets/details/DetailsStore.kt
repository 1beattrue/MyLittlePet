package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details

import android.widget.DatePicker
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
import java.time.LocalDate
import java.util.Calendar
import javax.inject.Inject

interface DetailsStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object ClickBack : Intent
        data class SetAge(val dateOfBirth: LocalDate) : Intent
        data class SetWeight(val weight: Int) : Intent

        data object OpenDatePickerDialog : Intent
        data object CloseDatePickerDialog : Intent

        data object OnAddEventClick : Intent
        data object OnAddNoteClick : Intent
        data object OnAddMedicalDataClick : Intent

        data object CloseBottomSheet : Intent

        data class AddEvent(val event: Event) : Intent
        data class AddNote(val note: Note) : Intent
        data class AddMedicalData(val medicalData: MedicalData) : Intent
    }

    data class State(
        val age: String?,
        val weight: Int?,

        val eventBottomSheetState: Boolean,
        val noteBottomSheetState: Boolean,
        val medicalDataBottomSheetState: Boolean,

        val datePickerDialogState: Boolean,

        val eventList: List<Event>,
        val noteList: List<Note>,
        val medicalDataList: List<MedicalData>,
    )

    sealed interface Label {
        data object ClickBack : Label
    }
}

class DetailsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val editPetUseCase: EditPetUseCase
) {

    fun create(pet: Pet): DetailsStore =
        object : DetailsStore, Store<Intent, State, Label> by storeFactory.create(
            name = "DetailsStore",
            initialState = State(
                age = pet.age,
                weight = pet.weight,

                eventBottomSheetState = false,
                noteBottomSheetState = false,
                medicalDataBottomSheetState = false,

                datePickerDialogState = false,

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
        data class SetAge(val age: String) : Msg
        data class SetWeight(val weight: Int) : Msg

        data object OpenDatePickerDialog : Msg
        data object CloseDatePickerDialog : Msg

        data object OpenEventBottomSheet : Msg
        data object OpenNoteBottomSheet : Msg
        data object OpenMedicalDataBottomSheet : Msg
        data object CloseBottomSheet : Msg

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
                    val localDate = intent.dateOfBirth

                    val year = localDate.year
                    val month = localDate.monthValue
                    val day = localDate.dayOfMonth

                    val age = calculateAge(year, month, day)

                    dispatch(Msg.SetAge(age))
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

                Intent.OnAddEventClick -> {
                    dispatch(Msg.OpenEventBottomSheet)
                }

                Intent.OnAddMedicalDataClick -> {
                    dispatch(Msg.OpenMedicalDataBottomSheet)
                }

                Intent.OnAddNoteClick -> {
                    dispatch(Msg.OpenNoteBottomSheet)
                }

                Intent.CloseBottomSheet -> {
                    dispatch(Msg.CloseBottomSheet)
                }

                Intent.OpenDatePickerDialog -> {
                    dispatch(Msg.OpenDatePickerDialog)
                }

                Intent.CloseDatePickerDialog -> {
                    dispatch(Msg.CloseDatePickerDialog)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.SetAge -> copy(age = msg.age, datePickerDialogState = false)
                is Msg.SetWeight -> copy(weight = msg.weight)

                is Msg.UpdateEvents -> copy(eventList = msg.events, eventBottomSheetState = false)
                is Msg.UpdateNotes -> copy(noteList = msg.notes, noteBottomSheetState = false)
                is Msg.UpdateMedicalDatas -> copy(medicalDataList = msg.medicalDatas, medicalDataBottomSheetState = false)

                Msg.OpenEventBottomSheet -> copy(eventBottomSheetState = true)
                Msg.OpenNoteBottomSheet -> copy(noteBottomSheetState = true)
                Msg.OpenMedicalDataBottomSheet -> copy(medicalDataBottomSheetState = true)

                Msg.CloseBottomSheet -> copy(
                    eventBottomSheetState = false,
                    noteBottomSheetState = false,
                    medicalDataBottomSheetState = false
                )

                Msg.OpenDatePickerDialog -> copy(datePickerDialogState = true)
                Msg.CloseDatePickerDialog -> copy(datePickerDialogState = false)
            }
    }

    private fun calculateAge(year: Int, month: Int, day: Int): String {
        val dob = Calendar.getInstance()
        dob.set(year, month - 1, day)

        val today = Calendar.getInstance()

        var years = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
        var months = today.get(Calendar.MONTH) - dob.get(Calendar.MONTH)
        var days = today.get(Calendar.DAY_OF_MONTH) - dob.get(Calendar.DAY_OF_MONTH)

        if (months < 0 || (months == 0 && days < 0)) {
            years--
            months += 12
        }

        val ageString = if (years > 0) {
            if (months > 0) {
                "$years лет $months месяцев"
            } else {
                "$years лет"
            }
        } else {
            if (months > 0) {
                "$months месяцев"
            } else {
                "Менее месяца"
            }
        }

        return ageString
    }
}
