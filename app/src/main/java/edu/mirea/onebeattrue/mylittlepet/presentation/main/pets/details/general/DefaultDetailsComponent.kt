package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.general

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.presentation.utils.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultDetailsComponent @AssistedInject constructor(
    private val storeFactory: DetailsStoreFactory,

    @Assisted("pet") override val pet: Pet,
    @Assisted("onClickBack") private val onClickBack: () -> Unit,
    @Assisted("onClickOpenEventList") private val onClickOpenEventList: () -> Unit,
    @Assisted("onClickOpenNoteList") private val onClickOpenNoteList: () -> Unit,
    @Assisted("onClickOpenMedicalDataList") private val onClickOpenMedicalDataList: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : DetailsComponent, ComponentContext by componentContext {
    val store = instanceKeeper.getStore { storeFactory.create(pet) }

    init {
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    DetailsStore.Label.ClickBack -> {
                        onClickBack()
                    }

                    DetailsStore.Label.OpenEventList -> {
                        onClickOpenEventList()
                    }

                    DetailsStore.Label.OpenMedicalDataList -> {
                        onClickOpenMedicalDataList()
                    }

                    DetailsStore.Label.OpenNoteList -> {
                        onClickOpenNoteList()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<DetailsStore.State>
        get() = store.stateFlow

    override fun setAge(age: Long) {
        store.accept(DetailsStore.Intent.SetAge(age))
    }

    override fun onChangeWeightClick() {
        store.accept(DetailsStore.Intent.OnChangeWeightClick)
    }

    override fun onWeightChanges(weight: String) {
        store.accept(DetailsStore.Intent.OnWeightChanged(weight))
    }

    override fun setWeight() {
        store.accept(DetailsStore.Intent.SetWeight)
    }

    override fun onCloseBottomSheetClick() {
        store.accept(DetailsStore.Intent.CloseBottomSheet)
    }

    override fun onBackClicked() {
        store.accept(DetailsStore.Intent.ClickBack)
    }

    override fun openDatePickerDialog() {
        store.accept(DetailsStore.Intent.OpenDatePickerDialog)
    }

    override fun closeDatePickerDialog() {
        store.accept(DetailsStore.Intent.CloseDatePickerDialog)
    }

    override fun onClickEventList() {
        store.accept(DetailsStore.Intent.OpenEventList)
    }

    override fun onClickNoteList() {
        store.accept(DetailsStore.Intent.OpenNoteList)
    }

    override fun onClickMedicalDataList() {
        store.accept(DetailsStore.Intent.OpenMedicalDataList)
    }

    override fun showQrCode() {
        store.accept(DetailsStore.Intent.ShowQrCode)
    }

    override fun hideQrCode() {
        store.accept(DetailsStore.Intent.HideQrCode)
    }

    @AssistedFactory
    interface Factory {
        fun create(

            @Assisted("pet") pet: Pet,
            @Assisted("onClickBack") onClickBack: () -> Unit,
            @Assisted("onClickOpenEventList") onClickOpenEventList: () -> Unit,
            @Assisted("onClickOpenNoteList") onClickOpenNoteList: () -> Unit,
            @Assisted("onClickOpenMedicalDataList") onClickOpenMedicalDataList: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultDetailsComponent
    }
}