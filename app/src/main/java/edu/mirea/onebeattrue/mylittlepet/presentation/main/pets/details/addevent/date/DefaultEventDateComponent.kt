package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.date

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.presentation.extensions.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultEventDateComponent @AssistedInject constructor(
    private val storeFactory: EventDateStoreFactory,

    @Assisted("eventText") private val eventText: String,
    @Assisted("eventTimeHours") private val eventTimeHours: Int,
    @Assisted("eventTimeMinutes") private val eventTimeMinutes: Int,
    @Assisted("pet") private val pet: Pet,
    @Assisted("onFinish") private val onFinish: () -> Unit,

    @Assisted("componentContext") componentContext: ComponentContext
) : EventDateComponent, ComponentContext by componentContext {
    private val store = instanceKeeper.getStore {
        storeFactory.create(
            eventText,
            eventTimeHours,
            eventTimeMinutes,
            pet
        )
    }

    init {
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    EventDateStore.Label.Finish -> {
                        onFinish()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<EventDateStore.State>
        get() = store.stateFlow

    override fun onEventDateChanged(dateMillis: Long) {
        store.accept(EventDateStore.Intent.ChangeDate(dateMillis))
    }

    override fun finish() {
        store.accept(EventDateStore.Intent.Finish)
    }

    @AssistedFactory
    interface Factory {
        fun create(

            @Assisted("eventText") eventText: String,
            @Assisted("eventTimeHours") eventTimeHours: Int,
            @Assisted("eventTimeMinutes") eventTimeMinutes: Int,
            @Assisted("pet") pet: Pet,
            @Assisted("onFinish") onFinish: () -> Unit,

            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultEventDateComponent
    }
}