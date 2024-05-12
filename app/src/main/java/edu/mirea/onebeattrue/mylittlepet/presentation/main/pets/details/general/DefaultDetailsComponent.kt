package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.general

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Event
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.presentation.extensions.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultDetailsComponent @AssistedInject constructor(
    private val storeFactory: DetailsStoreFactory,

    @Assisted("pet") override val pet: Pet,
    @Assisted("onAddEvent") private val onAddEvent: (List<Event>) -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : DetailsComponent, ComponentContext by componentContext {
    val store = instanceKeeper.getStore { storeFactory.create(pet) }

    init {
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    is DetailsStore.Label.AddEvent -> {
                        onAddEvent(it.eventList)
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

    override fun onWeightChages(weight: String) {
        store.accept(DetailsStore.Intent.OnWeightChanged(weight))
    }

    override fun setWeight() {
        store.accept(DetailsStore.Intent.SetWeight)
    }

    override fun onAddEventClick() {
        store.accept(DetailsStore.Intent.OnAddEventClick)
    }

    override fun onAddNoteClick() {
        store.accept(DetailsStore.Intent.OnAddNoteClick)
    }

    override fun onAddMedicalDataClick() {
        store.accept(DetailsStore.Intent.OnAddMedicalDataClick)
    }

    override fun onDeleteEvent(event: Event) {
        store.accept(DetailsStore.Intent.DeleteEvent(event))
    }

    override fun onCloseBottomSheetClick() {
        store.accept(DetailsStore.Intent.CloseBottomSheet)
    }

    override fun addNote() {
        store.accept(DetailsStore.Intent.AddNote)
    }

    override fun addMedicalData() {
        store.accept(DetailsStore.Intent.AddMedicalData)
    }

    override fun openDatePickerDialog() {
        store.accept(DetailsStore.Intent.OpenDatePickerDialog)
    }

    override fun closeDatePickerDialog() {
        store.accept(DetailsStore.Intent.CloseDatePickerDialog)
    }

    @AssistedFactory
    interface Factory {
        fun create(

            @Assisted("pet") pet: Pet,
            @Assisted("onAddEvent") onAddEvent: (List<Event>) -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultDetailsComponent
    }
}