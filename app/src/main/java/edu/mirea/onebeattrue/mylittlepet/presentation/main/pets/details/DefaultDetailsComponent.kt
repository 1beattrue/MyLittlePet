package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details

import android.widget.DatePicker
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Event
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalData
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Note
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.presentation.extensions.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultDetailsComponent @AssistedInject constructor(
    private val storeFactory: DetailsStoreFactory,

    @Assisted("onBackClick") private val onBackClick: () -> Unit,
    @Assisted("pet") override val pet: Pet,
    @Assisted("componentContext") componentContext: ComponentContext
) : DetailsComponent, ComponentContext by componentContext {
    val store = instanceKeeper.getStore { storeFactory.create(pet) }

    init {
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    DetailsStore.Label.ClickBack -> {
                        onBackClick()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<DetailsStore.State>
        get() = store.stateFlow

    override fun setAge(age: DatePicker) {
        store.accept(DetailsStore.Intent.SetAge(age))
    }

    override fun setWeight(weight: Int) {
        store.accept(DetailsStore.Intent.SetWeight(weight))
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

    override fun onCloseBottomSheetClick() {
        store.accept(DetailsStore.Intent.CloseBottomSheet)
    }

    override fun addEvent(event: Event) {
        store.accept(DetailsStore.Intent.AddEvent(event))
    }

    override fun addNote(note: Note) {
        store.accept(DetailsStore.Intent.AddNote(note))
    }

    override fun addMedicalData(medicalData: MedicalData) {
        store.accept(DetailsStore.Intent.AddMedicalData(medicalData))
    }

    @AssistedFactory
    interface Factory {
        fun create(

            @Assisted("pet") pet: Pet,
            @Assisted("onBackClick") onBackClick: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultDetailsComponent
    }
}