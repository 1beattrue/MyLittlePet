package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.medicaldatalist

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalData
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.presentation.extensions.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultMedicalDataListComponent @AssistedInject constructor(
    private val storeFactory: MedicalDataListStoreFactory,

    @Assisted("pet") private val pet: Pet,
    @Assisted("onAddMedicalDataClicked") private val onAddMedicalDataClicked: (List<MedicalData>) -> Unit,
    @Assisted("onClickBack") private val onClickBack: () -> Unit,
    @Assisted("onPhotoOpened") private val onPhotoOpened: (MedicalData) -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : MedicalDataListComponent, ComponentContext by componentContext {
    private val store = instanceKeeper.getStore { storeFactory.create(pet) }

    init {
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    is MedicalDataListStore.Label.OnAddMedicalDataClick -> {
                        onAddMedicalDataClicked(it.medicalDataList)
                    }

                    MedicalDataListStore.Label.OnClickBack -> {
                        onClickBack()
                    }

                    is MedicalDataListStore.Label.OnOpenPhoto -> {
                        onPhotoOpened(it.medicalData)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<MedicalDataListStore.State>
        get() = store.stateFlow

    override fun onAddMedicalData() {
        store.accept(MedicalDataListStore.Intent.AddMedicalData)
    }

    override fun onDeleteMedicalData(medicalData: MedicalData) {
        store.accept(MedicalDataListStore.Intent.DeleteMedicalData(medicalData))
    }

    override fun onOpenPhoto(medicalData: MedicalData) {
        store.accept(MedicalDataListStore.Intent.OnOpenPhoto(medicalData))
    }


    override fun onBackClicked() {
        store.accept(MedicalDataListStore.Intent.OnClickBack)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("pet") pet: Pet,
            @Assisted("onAddMedicalDataClicked") onAddMedicalDataClicked: (List<MedicalData>) -> Unit,
            @Assisted("onClickBack") onClickBack: () -> Unit,
            @Assisted("onPhotoOpened") onPhotoOpened: (MedicalData) -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultMedicalDataListComponent
    }
}