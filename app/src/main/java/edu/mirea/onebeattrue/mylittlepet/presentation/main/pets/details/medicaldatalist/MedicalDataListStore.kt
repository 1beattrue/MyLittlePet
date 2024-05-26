package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.medicaldatalist

import android.net.Uri
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalData
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.EditPetUseCase
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.GetPetByIdUseCase
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.medicaldatalist.MedicalDataListStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.medicaldatalist.MedicalDataListStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.medicaldatalist.MedicalDataListStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface MedicalDataListStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object AddMedicalData : Intent
        data class DeleteMedicalData(val medicalData: MedicalData) : Intent
        data object OnClickBack : Intent
        data class OnOpenPhoto(val medicalData: MedicalData) : Intent
    }

    data class State(
        val medicalDataList: List<MedicalData>
    )

    sealed interface Label {
        data class OnAddMedicalDataClick(val medicalDataList: List<MedicalData>) : Label
        data object OnClickBack : Label
        data class OnOpenPhoto(val medicalData: MedicalData) : Label
    }
}

class MedicalDataListStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val editPetUseCase: EditPetUseCase,
    private val getPetByIdUseCase: GetPetByIdUseCase
) {
    fun create(
        pet: Pet
    ): MedicalDataListStore =
        object : MedicalDataListStore, Store<Intent, State, Label> by storeFactory.create(
            name = "EventListStore",
            initialState = State(
                medicalDataList = pet.medicalDataList
            ),
            bootstrapper = BootstrapperImpl(pet),
            executorFactory = { ExecutorImpl(pet) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class UpdateMedicalDataList(val medicalDataList: List<MedicalData>) : Action
    }

    private sealed interface Msg {
        data class UpdateMedicalDataList(val medicalDataList: List<MedicalData>) : Msg
    }

    private inner class BootstrapperImpl(
        private val pet: Pet
    ) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getPetByIdUseCase(pet.id).collect { updatedPet ->
                    dispatch(Action.UpdateMedicalDataList(updatedPet.medicalDataList))
                }
            }
        }
    }

    private inner class ExecutorImpl(
        private val pet: Pet
    ) : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.UpdateMedicalDataList -> {
                    dispatch(Msg.UpdateMedicalDataList(action.medicalDataList))
                }
            }
        }

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.AddMedicalData -> {
                    val medicalDataList = getState().medicalDataList
                    publish(Label.OnAddMedicalDataClick(medicalDataList))
                }

                is Intent.DeleteMedicalData -> {
                    scope.launch {
                        val oldMedicalDataList = getState().medicalDataList
                        val newMedicalDataList = oldMedicalDataList
                            .toMutableList()
                            .apply {
                                removeIf {
                                    it.id == intent.medicalData.id
                                }
                            }
                            .toList()

                        editPetUseCase(pet.copy(medicalDataList = newMedicalDataList))
                    }
                }

                Intent.OnClickBack -> publish(Label.OnClickBack)
                is Intent.OnOpenPhoto -> {
                    val photoUri = Uri.parse(intent.medicalData.imageUri)
                    if (photoUri != Uri.EMPTY) {
                        publish(Label.OnOpenPhoto(intent.medicalData))
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.UpdateMedicalDataList -> {
                    copy(medicalDataList = msg.medicalDataList)
                }
            }
    }
}
