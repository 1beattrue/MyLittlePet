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
import edu.mirea.onebeattrue.mylittlepet.extensions.convertMillisToYearsAndMonths
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.DetailsStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.DetailsStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.DetailsStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface DetailsStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object ClickBack : Intent
        data class SetAge(val dateOfBirth: Long) : Intent
        data object SetWeight : Intent

        data object OnChangeWeightClick : Intent
        data class OnWeightChanged(val weight: String) : Intent

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
        val age: Age?,
        val weight: Float?,
        val weightInput: String,
        val isIncorrectWeight: Boolean,

        val weightBottomSheetState: Boolean,
        val eventBottomSheetState: Boolean,
        val noteBottomSheetState: Boolean,
        val medicalDataBottomSheetState: Boolean,

        val datePickerDialogState: Boolean,

        val eventList: List<Event>,
        val noteList: List<Note>,
        val medicalDataList: List<MedicalData>,
    ) {
        data class Age(
            val years: Int,
            val months: Int
        )
    }

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
                age = if (pet.dateOfBirth == null) null else State.Age(
                    years = pet.dateOfBirth.convertMillisToYearsAndMonths().first,
                    months = pet.dateOfBirth.convertMillisToYearsAndMonths().second
                ),
                weight = pet.weight,
                weightInput = pet.weight?.toString() ?: "",
                isIncorrectWeight = false,

                weightBottomSheetState = false,
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
        data class SetAge(val age: State.Age) : Msg
        data class SetWeight(val weight: Float) : Msg
        data class OpenWeightBottomSheet(val weight: String) : Msg
        data class OnWeightChanged(val weight: String) : Msg
        data object OnIncorrectWeight : Msg

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
                    scope.launch {
                        val age = State.Age(
                            years = intent.dateOfBirth.convertMillisToYearsAndMonths().first,
                            months = intent.dateOfBirth.convertMillisToYearsAndMonths().second
                        )
                        editPetUseCase(pet.copy(dateOfBirth = intent.dateOfBirth))
                        dispatch(Msg.SetAge(age))
                    }

                }

                is Intent.SetWeight -> {
                    scope.launch {
                        val weight = getState().weightInput
                        if (isCorrectWeight(weight)) {
                            editPetUseCase(pet.copy(weight = weight.toFloat()))
                            dispatch(Msg.SetWeight(weight.toFloat()))
                        } else {
                            dispatch(Msg.OnIncorrectWeight)
                        }
                    }
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

                Intent.OnChangeWeightClick -> {
                    val weight = getState().weight?.toString() ?: ""
                    dispatch(Msg.OpenWeightBottomSheet(weight))
                }

                is Intent.OnWeightChanged -> {
                    val weight = formattedWeight(intent.weight)
                    dispatch(Msg.OnWeightChanged(weight))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.SetAge -> copy(age = msg.age, datePickerDialogState = false)
                is Msg.SetWeight -> copy(weight = msg.weight, weightBottomSheetState = false)

                is Msg.UpdateEvents -> copy(eventList = msg.events, eventBottomSheetState = false)
                is Msg.UpdateNotes -> copy(noteList = msg.notes, noteBottomSheetState = false)
                is Msg.UpdateMedicalDatas -> copy(
                    medicalDataList = msg.medicalDatas,
                    medicalDataBottomSheetState = false
                )

                Msg.OpenEventBottomSheet -> copy(eventBottomSheetState = true)
                Msg.OpenNoteBottomSheet -> copy(noteBottomSheetState = true)
                Msg.OpenMedicalDataBottomSheet -> copy(medicalDataBottomSheetState = true)

                Msg.CloseBottomSheet -> copy(
                    weightBottomSheetState = false,
                    eventBottomSheetState = false,
                    noteBottomSheetState = false,
                    medicalDataBottomSheetState = false
                )

                Msg.OpenDatePickerDialog -> copy(datePickerDialogState = true)
                Msg.CloseDatePickerDialog -> copy(datePickerDialogState = false)

                is Msg.OnWeightChanged -> copy(weightInput = msg.weight, isIncorrectWeight = false)
                is Msg.OpenWeightBottomSheet -> copy(
                    weightBottomSheetState = true,
                    weightInput = msg.weight,
                    isIncorrectWeight = false
                )

                Msg.OnIncorrectWeight -> copy(isIncorrectWeight = true)
            }
    }

    private fun formattedWeight(weight: String): String = weight.replace(',', '.')

    private fun isCorrectWeight(weight: String): Boolean {
        try {
            weight.toFloat()
            return true
        } catch (_: Exception) {
            return false
        }
    }
}
