package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.general

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.EditPetUseCase
import edu.mirea.onebeattrue.mylittlepet.extensions.convertMillisToYearsAndMonths
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.general.DetailsStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.general.DetailsStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.general.DetailsStore.State
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

interface DetailsStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object OpenDatePickerDialog : Intent
        data object CloseDatePickerDialog : Intent
        data class SetAge(val dateOfBirth: Long) : Intent

        data object OnChangeWeightClick : Intent
        data class OnWeightChanged(val weight: String) : Intent
        data object SetWeight : Intent

        data object CloseBottomSheet : Intent

        data object ClickBack : Intent
        data object OpenEventList : Intent
        data object OpenNoteList : Intent
        data object OpenMedicalDataList : Intent

        data object ShowQrCode : Intent
        data object HideQrCode : Intent
    }

    data class State(
        val age: AgeState,
        val weight: WeightState,
        val bottomSheetMustBeClosed: Boolean,
        val isQrCodeOpen: Boolean
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
    }

    sealed interface Label {
        data object ClickBack : Label
        data object OpenEventList : Label
        data object OpenNoteList : Label
        data object OpenMedicalDataList : Label
    }
}

class DetailsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val editPetUseCase: EditPetUseCase,
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
                bottomSheetMustBeClosed = false,
                isQrCodeOpen = false
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
        data object CloseBottomSheet : Msg

        data object ShowQrCode : Msg
        data object HideQrCode : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl(
        private val pet: Pet
    ) : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
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
                            val roundedWeight = roundedWeight(weight.toFloat())
                            editPetUseCase(pet.copy(weight = roundedWeight))
                            dispatch(Msg.SetWeight(roundedWeight))
                        } else {
                            dispatch(Msg.OnIncorrectWeight)
                        }
                    }
                }

                Intent.ClickBack -> publish(Label.ClickBack)
                Intent.OpenEventList -> publish(Label.OpenEventList)
                Intent.OpenNoteList -> publish(Label.OpenNoteList)
                Intent.OpenMedicalDataList -> publish(Label.OpenMedicalDataList)

                Intent.ShowQrCode -> {
                    dispatch(Msg.ShowQrCode)
                }

                Intent.HideQrCode -> {
                    dispatch(Msg.HideQrCode)
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

                Msg.CloseBottomSheet -> copy(
                    bottomSheetMustBeClosed = false,
                    weight = weight.copy(bottomSheetState = false),
                )

                Msg.ShowQrCode -> copy(isQrCodeOpen = true)

                Msg.HideQrCode -> copy(isQrCodeOpen = false)
            }
    }

    private fun formattedWeight(weight: String): String {
        val formattedWeight = weight.replace(',', '.')
        if (formattedWeight.length > 10) return formattedWeight.substring(0 until 10)
        return formattedWeight
    }

    private fun roundedWeight(weight: Float): Float {
        val currentLocale = Locale.getDefault()
        return String.format(currentLocale, "%.2f", weight).toFloat()
    }

    private fun isCorrectWeight(weight: String): Boolean {
        try {
            val weightValue = weight.toFloat()
            return weightValue in 0f..1000f
        } catch (_: Exception) {
            return false
        }
    }
}
