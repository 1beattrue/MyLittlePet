package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details

import android.icu.util.Calendar
import android.net.Uri
import androidx.compose.ui.Modifier
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.mylittlepet.R
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
import java.time.LocalDate
import javax.inject.Inject

interface DetailsStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object ClickBack : Intent

        data object OpenDatePickerDialog : Intent
        data object CloseDatePickerDialog : Intent
        data class SetAge(val dateOfBirth: Long) : Intent

        data object OnChangeWeightClick : Intent
        data class OnWeightChanged(val weight: String) : Intent
        data object SetWeight : Intent

        data object OnAddEventClick : Intent
        data class OnEventChanged(val label: String) : Intent
        data object AddEvent : Intent

        data object OnAddNoteClick : Intent
        data object OnAddMedicalDataClick : Intent

        data object AddNote : Intent
        data object AddMedicalData : Intent

        data object CloseBottomSheet : Intent
    }

    data class State(
        val age: AgeState,
        val weight: WeightState,
        val event: EventState,
        val note: NoteState,
        val medicalData: MedicalDataState,
        val bottomSheetMustBeClosed: Boolean,
    ) {
        data class AgeState(
            val years: Int?,
            val months: Int?,
            val datePickerDialogState: Boolean
        )

        data class WeightState(
            val value: Float?,
            val changeableValue: String,
            val isIncorrect: Boolean,
            val bottomSheetState: Boolean
        )

        data class EventState(
            val list: List<Event>,
            val changeableLabel: String,
            val bottomSheetState: Boolean
        )

        data class NoteState(
            val list: List<Note>,
            val changeableText: String,
            val changebleIcon: Int,
            val bottomSheetState: Boolean
        )

        data class MedicalDataState(
            val list: List<MedicalData>,
            val changeableName: String,
            val datePickerDialogState: Boolean,
            val changeableDate: Long?,
            val timePickerDialogState: Boolean,
            val changeableTime: Long?,
            val imageUri: Uri,
            val changeableText: String,
            val bottomSheetState: Boolean
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
                age = if (pet.dateOfBirth == null) State.AgeState(
                    years = null,
                    months = null,
                    datePickerDialogState = false
                ) else State.AgeState(
                    years = pet.dateOfBirth.convertMillisToYearsAndMonths().first,
                    months = pet.dateOfBirth.convertMillisToYearsAndMonths().second,
                    datePickerDialogState = false
                ),
                weight = State.WeightState(
                    value = pet.weight,
                    changeableValue = pet.weight?.toString() ?: "",
                    isIncorrect = false,
                    bottomSheetState = false
                ),
                event = State.EventState(
                    list = pet.eventList,
                    changeableLabel = "",
                    bottomSheetState = false
                ),
                note = State.NoteState(
                    list = pet.noteList,
                    changebleIcon = R.drawable.ic_medical,
                    changeableText = "",
                    bottomSheetState = false
                ),
                medicalData = State.MedicalDataState(
                    list = pet.medicalDataList,
                    changeableName = "",
                    datePickerDialogState = false,
                    changeableDate = null,
                    timePickerDialogState = false,
                    changeableTime = null,
                    imageUri = Uri.EMPTY,
                    changeableText = "",
                    bottomSheetState = false
                ),
                bottomSheetMustBeClosed = false
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = { ExecutorImpl(pet) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {
        data object OpenDatePickerDialog : Msg
        data object CloseDatePickerDialog : Msg
        data class SetAge(val age: Long) : Msg

        data class OpenWeightBottomSheet(val weight: String) : Msg
        data class OnWeightChanged(val weight: String) : Msg
        data object OnIncorrectWeight : Msg
        data class SetWeight(val weight: Float) : Msg

        data object OpenEventBottomSheet : Msg
        data class OnEventChange(val label: String) : Msg
        data class UpdateEvents(val events: List<Event>) : Msg

        data object OpenNoteBottomSheet : Msg
        data class UpdateNotes(val notes: List<Note>) : Msg

        data object OpenMedicalDataBottomSheet : Msg
        data class UpdateMedicalDatas(val medicalDatas: List<MedicalData>) : Msg

        data object CloseBottomSheet : Msg
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
                Intent.ClickBack -> publish(Label.ClickBack)
                Intent.CloseBottomSheet -> dispatch(Msg.CloseBottomSheet)


                Intent.OpenDatePickerDialog -> dispatch(Msg.OpenDatePickerDialog)
                Intent.CloseDatePickerDialog -> dispatch(Msg.CloseDatePickerDialog)
                is Intent.SetAge -> {
                    scope.launch {
                        editPetUseCase(pet.copy(dateOfBirth = intent.dateOfBirth))
                        dispatch(Msg.SetAge(intent.dateOfBirth))
                    }
                }


                Intent.OnChangeWeightClick -> {
                    val weight = getState().weight.value?.toString() ?: ""
                    dispatch(Msg.OpenWeightBottomSheet(weight))
                }

                is Intent.OnWeightChanged -> {
                    val weight = formattedWeight(intent.weight)
                    dispatch(Msg.OnWeightChanged(weight))
                }

                Intent.SetWeight -> {
                    scope.launch {
                        val weight = getState().weight.changeableValue
                        if (isCorrectWeight(weight)) {
                            editPetUseCase(pet.copy(weight = weight.toFloat()))
                            dispatch(Msg.SetWeight(weight.toFloat()))
                        } else {
                            dispatch(Msg.OnIncorrectWeight)
                        }
                    }
                }


                Intent.OnAddEventClick -> dispatch(Msg.OpenEventBottomSheet)
                is Intent.OnEventChanged -> dispatch(Msg.OnEventChange(intent.label))
                Intent.AddEvent -> {
                    scope.launch {
                        val oldEventList = getState().event.list
                        val newEventList = oldEventList
                            .toMutableList()
                            .apply {
                                add(
                                    Event(
                                        date = Calendar.getInstance().timeInMillis,
                                        label = getState().event.changeableLabel
                                    )
                                )
                            }
                            .toList()
                        editPetUseCase(pet.copy(eventList = newEventList))
                        dispatch(Msg.UpdateEvents(newEventList))
                    }
                }


                Intent.OnAddNoteClick -> dispatch(Msg.OpenNoteBottomSheet)
                is Intent.AddNote -> {
                    scope.launch {
                        val oldNoteList = getState().note.list
                        val newNoteList = oldNoteList
                            .toMutableList()
                            .apply {
                                add(
                                    Note(
                                        text = getState().note.changeableText,
                                        iconResId = getState().note.changebleIcon
                                    )
                                )
                            }
                            .toList()
                        editPetUseCase(pet.copy(noteList = newNoteList))
                        dispatch(Msg.UpdateNotes(newNoteList))
                    }
                }


                Intent.OnAddMedicalDataClick -> dispatch(Msg.OpenMedicalDataBottomSheet)
                is Intent.AddMedicalData -> {
                    scope.launch {
                        val oldMedicalList = getState().medicalData.list
                        val newMedicalList = oldMedicalList
                            .toMutableList()
                            .apply {
                                add(
                                    MedicalData(
                                        name = getState().medicalData.changeableName,
                                        date = getState().medicalData.changeableDate ?: 0L,
                                        time = getState().medicalData.changeableTime ?: 0L,
                                        imageUri = getState().medicalData.imageUri,
                                        note = getState().medicalData.changeableText
                                    )
                                )
                            }
                            .toList()
                        editPetUseCase(pet.copy(medicalDataList = newMedicalList))
                        dispatch(Msg.UpdateMedicalDatas(newMedicalList))
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                Msg.OpenDatePickerDialog -> copy(age = age.copy(datePickerDialogState = true))
                Msg.CloseDatePickerDialog -> copy(age = age.copy(datePickerDialogState = false))
                is Msg.SetAge -> copy(
                    age = age.copy(
                        years = msg.age.convertMillisToYearsAndMonths().first,
                        months = msg.age.convertMillisToYearsAndMonths().second,
                        datePickerDialogState = false
                    )
                )

                is Msg.OpenWeightBottomSheet -> copy(
                    weight = weight.copy(
                        bottomSheetState = true,
                        changeableValue = msg.weight,
                        isIncorrect = false
                    )
                )

                is Msg.OnWeightChanged -> copy(
                    weight = weight.copy(
                        changeableValue = msg.weight,
                        isIncorrect = false
                    )
                )

                Msg.OnIncorrectWeight -> copy(
                    weight = weight.copy(
                        isIncorrect = true
                    )
                )

                is Msg.SetWeight -> copy(
                    weight = weight.copy(
                        value = msg.weight
                    ),
                    bottomSheetMustBeClosed = true
                )

                Msg.OpenEventBottomSheet -> copy(
                    event = event.copy(
                        bottomSheetState = true,
                        changeableLabel = ""
                    )
                )

                is Msg.OnEventChange -> copy(
                    event = event.copy(
                        changeableLabel = msg.label
                    )
                )

                is Msg.UpdateEvents -> copy(
                    event = event.copy(
                        list = msg.events,
                    ),
                    bottomSheetMustBeClosed = true
                )


                Msg.OpenNoteBottomSheet -> copy(
                    note = note.copy(
                        bottomSheetState = true,
                        // TODO()
                    )
                )

                is Msg.UpdateNotes -> copy(
                    // TODO(),
                    bottomSheetMustBeClosed = true
                )

                Msg.OpenMedicalDataBottomSheet -> copy(
                    medicalData = medicalData.copy(
                        bottomSheetState = true
                        // TODO()
                    )
                )

                is Msg.UpdateMedicalDatas -> copy(
                    // TODO(),
                    bottomSheetMustBeClosed = true
                )

                Msg.CloseBottomSheet -> copy(
                    bottomSheetMustBeClosed = false,
                    weight = weight.copy(bottomSheetState = false),
                    event = event.copy(bottomSheetState = false),
                    note = note.copy(bottomSheetState = false),
                    medicalData = medicalData.copy(bottomSheetState = false)
                )
            }
    }

    private fun formattedWeight(weight: String): String {
        val formattedWeight = weight.replace(',', '.')
        if (formattedWeight.length > 10) return formattedWeight.substring(0 until 10)
        return formattedWeight
    }

    private fun isCorrectWeight(weight: String): Boolean {
        try {
            weight.toFloat()
            return true
        } catch (_: Exception) {
            return false
        }
    }
}
