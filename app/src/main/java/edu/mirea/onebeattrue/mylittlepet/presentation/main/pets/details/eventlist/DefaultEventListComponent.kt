package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.eventlist

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Event
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.presentation.utils.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultEventListComponent @AssistedInject constructor(
    private val storeFactory: EventListStoreFactory,

    @Assisted("lastPet") private val pet: Pet,
    @Assisted("onAddEvent") private val onAddEventClick: (Pet) -> Unit,
    @Assisted("onClickBack") private val onClickBack: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : EventListComponent, ComponentContext by componentContext {
    private val store = instanceKeeper.getStore { storeFactory.create(pet) }

    init {
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    is EventListStore.Label.OnAddEventClick -> {
                        onAddEventClick(it.pet)
                    }

                    EventListStore.Label.OnClickBack -> onClickBack()
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<EventListStore.State>
        get() = store.stateFlow

    override fun onAddEvent() {
        store.accept(EventListStore.Intent.AddEvent)
    }

    override fun onDeleteEvent(event: Event) {
        store.accept(EventListStore.Intent.DeleteEvent(event))
    }

    override fun onBackClicked() {
        store.accept(EventListStore.Intent.OnClickBack)
    }

    override fun onDeletePastEvents() {
        store.accept(EventListStore.Intent.DeletePastEvents)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("lastPet") pet: Pet,
            @Assisted("onAddEvent") onAddEvent: (Pet) -> Unit,
            @Assisted("onClickBack") onClickBack: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultEventListComponent
    }
}