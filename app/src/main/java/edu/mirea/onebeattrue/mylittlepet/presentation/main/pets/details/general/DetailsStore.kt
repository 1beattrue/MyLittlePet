package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.general

import android.graphics.Bitmap
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.EditPetUseCase
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.GenerateQrCodeUseCase
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.GetPetByIdUseCase
import edu.mirea.onebeattrue.mylittlepet.extensions.convertMillisToYearsAndMonths
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.general.DetailsStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.general.DetailsStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.general.DetailsStore.State
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

interface DetailsStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object OnChangeAgeClick : Intent
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
        val pet: Pet,
        val age: AgeState,
        val weight: WeightState,
        val bottomSheetMustBeClosed: Boolean,
        val qrCode: QrCode,
        val progress: Boolean
    ) {
        data class AgeState(
            val isError: Boolean,
            val years: Int?,
            val months: Int?,
            val bottomSheetState: Boolean
        )

        data class WeightState(
            val isError: Boolean,
            val value: Float?,
            val changeableValue: String,
            val isIncorrect: Boolean,
            val bottomSheetState: Boolean
        )

        data class QrCode(
            val isOpen: Boolean,
            val bitmap: Bitmap?
        )
    }

    sealed interface Label {
        data object ClickBack : Label
        data class OpenEventList(val pet: Pet) : Label
        data class OpenNoteList(val pet: Pet) : Label
        data class OpenMedicalDataList(val pet: Pet) : Label
    }
}

class DetailsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val editPetUseCase: EditPetUseCase,
    private val generateQrCodeUseCase: GenerateQrCodeUseCase,
    private val getPetByIdUseCase: GetPetByIdUseCase
) {

    fun create(pet: Pet): DetailsStore =
        object : DetailsStore, Store<Intent, State, Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = State(
                age = if (pet.dateOfBirth == null) State.AgeState(
                    years = null,
                    months = null,
                    bottomSheetState = false,
                    isError = false
                ) else State.AgeState(
                    years = pet.dateOfBirth.convertMillisToYearsAndMonths().first,
                    months = pet.dateOfBirth.convertMillisToYearsAndMonths().second,
                    bottomSheetState = false,
                    isError = false
                ),
                weight = State.WeightState(
                    value = pet.weight,
                    changeableValue = pet.weight?.toString() ?: "",
                    isIncorrect = false,
                    bottomSheetState = false,
                    isError = false
                ),
                bottomSheetMustBeClosed = false,
                qrCode = State.QrCode(
                    isOpen = false,
                    bitmap = null
                ),
                pet = pet,
                progress = false
            ),
            bootstrapper = BootstrapperImpl(pet),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class UpdatePet(val pet: Pet) : Action
    }

    private sealed interface Msg {
        data object OpenAgeBottomSheet : Msg
        data class SetAge(val age: Long) : Msg

        data class OpenWeightBottomSheet(val weight: String) : Msg
        data class OnWeightChanged(val weight: String) : Msg
        data object OnIncorrectWeight : Msg
        data class SetWeight(val weight: Float) : Msg

        data object CloseBottomSheet : Msg

        data object EditAgeError : Msg
        data object EditWeightError : Msg

        data class ShowQrCode(
            val bitmap: Bitmap
        ) : Msg

        data object HideQrCode : Msg

        data class UpdatePet(val pet: Pet) : Msg

        data object Loading : Msg
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
                is Action.UpdatePet -> dispatch(Msg.UpdatePet(action.pet))
            }
        }

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.CloseBottomSheet -> dispatch(Msg.CloseBottomSheet)

                Intent.OnChangeAgeClick -> {
                    dispatch(Msg.OpenAgeBottomSheet)
                }

                Intent.OnChangeWeightClick -> {
                    val weight = getState().weight.value?.toString() ?: ""
                    dispatch(Msg.OpenWeightBottomSheet(weight))
                }

                is Intent.OnWeightChanged -> {
                    val weight = formattedWeight(intent.weight)
                    dispatch(Msg.OnWeightChanged(weight))
                }

                is Intent.SetAge -> {
                    scope.launch {
                        val pet = getState().pet

                        try {
                            dispatch(Msg.Loading)
                            editPetUseCase(pet.copy(dateOfBirth = intent.dateOfBirth))
                            dispatch(Msg.SetAge(intent.dateOfBirth))
                        } catch (_: Exception) {
                            dispatch(Msg.EditAgeError)
                        }
                    }
                }

                Intent.SetWeight -> {
                    scope.launch {
                        val pet = getState().pet
                        val weight = getState().weight.changeableValue

                        try {
                            if (isCorrectWeight(weight)) {
                                dispatch(Msg.Loading)
                                val roundedWeight = roundedWeight(weight.toFloat())
                                editPetUseCase(pet.copy(weight = roundedWeight))
                                dispatch(Msg.SetWeight(roundedWeight))
                            } else {
                                dispatch(Msg.OnIncorrectWeight)
                            }
                        } catch (_: Exception) {
                            dispatch(Msg.EditWeightError)
                        }
                    }
                }

                Intent.ClickBack -> publish(Label.ClickBack)

                Intent.OpenEventList -> {
                    val pet = getState().pet
                    publish(Label.OpenEventList(pet))
                }

                Intent.OpenNoteList -> {
                    val pet = getState().pet
                    publish(Label.OpenNoteList(pet))
                }

                Intent.OpenMedicalDataList -> {
                    val pet = getState().pet
                    publish(Label.OpenMedicalDataList(pet))
                }

                Intent.ShowQrCode -> {
                    scope.launch {
                        val pet = getState().pet
                        val qrCode = generateQrCodeUseCase(pet)
                        dispatch(Msg.ShowQrCode(qrCode))
                    }
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

                is Msg.OpenWeightBottomSheet -> copy(
                    weight = weight.copy(
                        bottomSheetState = true,
                        changeableValue = msg.weight,
                        isIncorrect = false
                    )
                )

                Msg.OpenAgeBottomSheet -> copy(
                    age = age.copy(
                        bottomSheetState = true,
                    )
                )

                is Msg.OnWeightChanged -> copy(
                    weight = weight.copy(
                        changeableValue = msg.weight,
                        isIncorrect = false,
                        isError = false
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

                is Msg.SetAge -> copy(
                    age = age.copy(
                        years = msg.age.convertMillisToYearsAndMonths().first,
                        months = msg.age.convertMillisToYearsAndMonths().second
                    ),
                    bottomSheetMustBeClosed = true
                )

                Msg.CloseBottomSheet -> copy(
                    bottomSheetMustBeClosed = false,
                    weight = weight.copy(
                        isIncorrect = false,
                        bottomSheetState = false,
                        isError = false
                    ),
                    age = age.copy(
                        bottomSheetState = false,
                        isError = false
                    ),
                    progress = false
                )

                is Msg.ShowQrCode -> copy(qrCode = State.QrCode(isOpen = true, bitmap = msg.bitmap))

                Msg.HideQrCode -> copy(qrCode = qrCode.copy(isOpen = false, bitmap = null))
                is Msg.UpdatePet -> copy(pet = msg.pet)

                Msg.EditAgeError -> copy(age = age.copy(isError = true), progress = false)
                Msg.EditWeightError -> copy(weight = weight.copy(isError = true), progress = false)

                Msg.Loading -> copy(
                    progress = true,
                    weight = weight.copy(isError = false),
                    age = age.copy(isError = false)
                )
            }
    }

    private fun formattedWeight(weight: String): String {
        val formattedWeight = weight.replace(',', '.')
        if (formattedWeight.length > 10) return formattedWeight.substring(0 until 10)
        return formattedWeight
    }

    private fun roundedWeight(weight: Float): Float {
        val currentLocale = Locale.ENGLISH
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

    companion object {
        private const val STORE_NAME = "DetailsStore"
    }
}
