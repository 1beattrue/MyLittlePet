package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.eventlist

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Event
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.DeleteEventUseCase
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.DeleteIrrelevantEventsUseCase
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.GetEventListUseCase
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.SynchronizeEventsWithServerUseCase
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.eventlist.EventListStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.eventlist.EventListStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.eventlist.EventListStore.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

interface EventListStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object AddEvent : Intent
        data class DeleteEvent(val event: Event) : Intent
        data object DeletePastEvents : Intent
        data object OnClickBack : Intent
        data object Synchronize : Intent
    }

    data class State(
        val isLoading: Boolean,
        val syncError: Boolean,
        val eventList: List<Event>,
        val deleteEventErrorId: Int?,
        val nowDeletingId: Int?,
        val isDeletingIrrelevantEvents: Boolean,
        val deleteIrrelevantEventsError: Boolean
    )

    sealed interface Label {
        data class OnAddEventClick(val pet: Pet) : Label
        data object OnClickBack : Label
    }
}

class EventListStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getEventListUseCase: GetEventListUseCase,
    private val deleteEventUseCase: DeleteEventUseCase,
    private val deleteIrrelevantEventsUseCase: DeleteIrrelevantEventsUseCase,
    private val synchronizeEventsWithServerUseCase: SynchronizeEventsWithServerUseCase
) {

    fun create(
        pet: Pet
    ): EventListStore =
        object : EventListStore, Store<Intent, State, Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = State(
                isLoading = false,
                syncError = false,
                eventList = pet.eventList,
                deleteEventErrorId = null,
                nowDeletingId = null,
                deleteIrrelevantEventsError = false,
                isDeletingIrrelevantEvents = false
            ),
            bootstrapper = BootstrapperImpl(pet),
            executorFactory = {
                ExecutorImpl(pet)
            },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data object Loading : Action
        data class UpdateList(val events: List<Event>) : Action
        data class SyncResult(val isError: Boolean) : Action
    }

    private sealed interface Msg {
        data object Loading : Msg
        data object DeletingIrrelevantEvents : Msg
        data class UpdateList(val events: List<Event>) : Msg
        data class SyncResult(val isError: Boolean) : Msg
        data class DeleteEventError(val eventId: Int) : Msg
        data object DeleteIrrelevantEventsError : Msg
        data class DeletingEvent(val id: Int) : Msg
    }

    private inner class BootstrapperImpl(
        private val pet: Pet
    ) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                dispatch(Action.Loading)
                try {
                    synchronize(
                        petName = pet.name,
                        petId = pet.id
                    )
                    dispatch(Action.SyncResult(isError = false))
                } catch (_: Exception) {
                    dispatch(Action.SyncResult(isError = true))
                } finally {
                    getEventListUseCase(pet.id).collect { updatedList ->
                        dispatch(Action.UpdateList(updatedList))
                    }
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

                Action.Loading -> {
                    dispatch(Msg.Loading)
                }

                is Action.SyncResult -> {
                    dispatch(Msg.SyncResult(action.isError))
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
                        dispatch(Msg.DeletingEvent(intent.event.id))
                        try {
                            withContext(Dispatchers.IO) {
                                deleteEventUseCase(
                                    petName = pet.name,
                                    event = intent.event
                                )
                            }
                        } catch (_: Exception) {
                            dispatch(Msg.DeleteEventError(intent.event.id))
                        }
                    }
                }

                Intent.DeletePastEvents -> {
                    scope.launch {
                        dispatch(Msg.DeletingIrrelevantEvents)
                        try {
                            withContext(Dispatchers.IO) {
                                deleteIrrelevantEventsUseCase(
                                    petName = pet.name,
                                    petId = pet.id
                                )
                            }
                            dispatch(Msg.SyncResult(isError = false))
                        } catch (_: Exception) {
                            dispatch(Msg.DeleteIrrelevantEventsError)
                        }
                    }
                }

                Intent.OnClickBack -> publish(Label.OnClickBack)
                Intent.Synchronize -> {
                    scope.launch {
                        dispatch(Msg.Loading)
                        try {
                            synchronize(
                                petName = pet.name,
                                petId = pet.id
                            )
                            dispatch(Msg.SyncResult(isError = false))
                        } catch (_: Exception) {
                            dispatch(Msg.SyncResult(isError = true))
                        }
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.UpdateList -> copy(
                    isLoading = false,
                    eventList = msg.events,
                    deleteEventErrorId = null,
                    deleteIrrelevantEventsError = false
                )

                Msg.Loading -> copy(
                    isLoading = true,
                    deleteEventErrorId = null,
                    deleteIrrelevantEventsError = false
                )

                is Msg.SyncResult -> copy(
                    syncError = msg.isError,
                    isLoading = false,
                    deleteEventErrorId = null,
                    deleteIrrelevantEventsError = false
                )

                is Msg.DeleteEventError -> copy(
                    deleteEventErrorId = msg.eventId,
                    nowDeletingId = null,
                    deleteIrrelevantEventsError = false
                )

                is Msg.DeletingEvent -> copy(
                    nowDeletingId = msg.id,
                    deleteEventErrorId = null,
                    deleteIrrelevantEventsError = false
                )

                Msg.DeleteIrrelevantEventsError -> copy(
                    isDeletingIrrelevantEvents = false,
                    deleteIrrelevantEventsError = true,
                )

                Msg.DeletingIrrelevantEvents -> copy(
                    isDeletingIrrelevantEvents = true,
                    deleteIrrelevantEventsError = false
                )
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

    private suspend fun synchronize(petName: String, petId: Int) {
        withContext(Dispatchers.IO) {
            synchronizeEventsWithServerUseCase(petName, petId)
        }
    }

    companion object {
        private const val STORE_NAME = "EventListStore"
    }
}
