package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.time

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

class DefaultEventTimeComponent @AssistedInject constructor(
    private val storeFactory: EventTimeStoreFactory,

    @Assisted("eventText") private val eventText: String,
    @Assisted("pet") private val pet: Pet,

    @Assisted("onNextClicked") private val onNextClicked: (Int, Int) -> Unit,
    @Assisted("onFinish") private val onFinish: () -> Unit,

    @Assisted("componentContext") componentContext: ComponentContext,
) : EventTimeComponent, ComponentContext by componentContext {
    private val store = instanceKeeper.getStore { storeFactory.create(eventText, pet) }

    init {
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    EventTimeStore.Label.Finish -> {
                        onFinish()
                    }

                    is EventTimeStore.Label.GoNext -> {
                        onNextClicked(it.hours, it.minutes)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<EventTimeStore.State>
        get() = store.stateFlow

    override fun onPeriodChanged(isDaily: Boolean) {
        store.accept(EventTimeStore.Intent.ChangePeriod(isDaily))
    }

    override fun next(hours: Int, minutes: Int) {
        store.accept(EventTimeStore.Intent.GoNext(hours, minutes))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("eventText") eventText: String,
            @Assisted("pet") pet: Pet,
            @Assisted("onNextClicked") onNextClicked: (Int, Int) -> Unit,
            @Assisted("onFinish") onFinish: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultEventTimeComponent
    }
}