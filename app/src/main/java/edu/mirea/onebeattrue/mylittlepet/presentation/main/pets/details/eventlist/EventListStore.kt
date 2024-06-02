package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.eventlist

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.AlarmItem
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Event
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.DeleteEventUseCase
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.GetEventListUseCase
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.eventlist.EventListStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.eventlist.EventListStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.eventlist.EventListStore.State
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

interface EventListStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object AddEvent : Intent
        data class DeleteEvent(val event: Event) : Intent
        data object DeletePastEvents : Intent
        data object OnClickBack : Intent
    }

    data class State(
        val eventList: List<Event>
    )

    sealed interface Label {
        data class OnAddEventClick(val pet: Pet) : Label
        data object OnClickBack : Label
    }
}

class EventListStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getEventListUseCase: GetEventListUseCase,
    private val deleteEventUseCase: DeleteEventUseCase
) {

    fun create(
        pet: Pet
    ): EventListStore =
        object : EventListStore, Store<Intent, State, Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = State(
                eventList = pet.eventList
            ),
            bootstrapper = BootstrapperImpl(pet),
            executorFactory = {
                ExecutorImpl(pet)
            },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class UpdateList(val events: List<Event>) : Action
    }

    private sealed interface Msg {
        data class UpdateList(val events: List<Event>) : Msg
    }

    private inner class BootstrapperImpl(
        private val pet: Pet
    ) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getEventListUseCase(pet.id).collect { updatedList ->
                    dispatch(Action.UpdateList(updatedList))
                }
            }
        }
    }

    private inner class ExecutorImpl(
        private val pet: Pet
    ) : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.UpdateList -> {
                    val sortedEvents = sortedEventList(action.events)
                    dispatch(Msg.UpdateList(sortedEvents))
                }
            }
        }

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.AddEvent -> {
                    publish(Label.OnAddEventClick(pet))
                }

                is Intent.DeleteEvent -> {
                    scope.launch {
                        deleteEventUseCase(petName = pet.name, event = intent.event)
                    }
                }

                Intent.DeletePastEvents -> {
                    scope.launch {
                        val currentTime = Calendar.getInstance().timeInMillis

                        val oldEventList = getState().eventList
                        oldEventList.filter { it.time <= currentTime && !it.repeatable }
                            .forEach { event ->
                                deleteEventUseCase(petName = pet.name, event = event)
                            }
                    }
                }

                Intent.OnClickBack -> publish(Label.OnClickBack)
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.UpdateList -> {
                    copy(eventList = msg.events)
                }
            }
    }

    private fun sortedEventList(list: List<Event>): List<Event> {
        return list.sortedWith(
            compareByDescending<Event> { it.repeatable }
                .thenByDescending {
                    if (it.repeatable) getTimeInHoursAndMinutes(
                        it.time
                    ) else it.time
                }
        )
    }

    private fun getTimeInHoursAndMinutes(time: Long): Int {
        val calendar = Calendar.getInstance().apply { timeInMillis = time }
        return calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)
    }

    companion object {
        private const val STORE_NAME = "EventListStore"
    }
}
