package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.eventlist

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.AlarmItem
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.AlarmScheduler
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Event
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.EditPetUseCase
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.GetPetByIdUseCase
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
        val pet: Pet
    )

    sealed interface Label {
        data class OnAddEventClick(val pet: Pet) : Label
        data object OnClickBack : Label
    }
}

class EventListStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val editPetUseCase: EditPetUseCase,
    private val alarmScheduler: AlarmScheduler,
    private val getPetByIdUseCase: GetPetByIdUseCase
) {

    fun create(
        pet: Pet
    ): EventListStore =
        object : EventListStore, Store<Intent, State, Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = State(
                pet = pet
            ),
            bootstrapper = BootstrapperImpl(pet),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class UpdatePet(val pet: Pet) : Action
    }

    private sealed interface Msg {
        data class UpdatePet(val pet: Pet) : Msg
    }

    private inner class BootstrapperImpl(
        private val pet: Pet
    ) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getPetByIdUseCase(pet.id).collect { updatedPet ->
                    dispatch(Action.UpdatePet(updatedPet))
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.UpdatePet -> {
                    val pet = action.pet
                    val sortedEvents = sortedEventList(action.pet.eventList)
                    val petWithSortedEvents = pet.copy(eventList = sortedEvents)
                    dispatch(Msg.UpdatePet(petWithSortedEvents))
                }
            }
        }

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.AddEvent -> {
                    val pet = getState().pet
                    publish(Label.OnAddEventClick(pet))
                }

                is Intent.DeleteEvent -> {
                    scope.launch {
                        val pet = getState().pet

                        val oldEventList = pet.eventList
                        val newEventList = oldEventList
                            .toMutableList()
                            .apply {
                                removeIf {
                                    it.id == intent.event.id
                                }
                            }
                            .toList()

                        cancelNotification(
                            time = intent.event.time,
                            title = pet.name,
                            text = intent.event.label,
                            repeatable = intent.event.repeatable
                        )

                        editPetUseCase(pet.copy(eventList = newEventList))
                    }
                }

                Intent.DeletePastEvents -> {
                    scope.launch {
                        val pet = getState().pet

                        val currentTime = Calendar.getInstance().timeInMillis

                        val oldEventList = getState().pet.eventList
                        oldEventList.filter { it.time <= currentTime && !it.repeatable }
                            .forEach { event ->
                                cancelNotification(
                                    time = event.time,
                                    title = pet.name,
                                    text = event.label,
                                    repeatable = event.repeatable
                                )
                            }

                        val newEventList = oldEventList
                            .filter { it.time > currentTime || it.repeatable }

                        editPetUseCase(pet.copy(eventList = newEventList))
                    }
                }

                Intent.OnClickBack -> publish(Label.OnClickBack)
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.UpdatePet -> {
                    copy(pet = msg.pet)
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

    private fun cancelNotification(
        time: Long,
        title: String,
        text: String,
        repeatable: Boolean
    ) {
        alarmScheduler.cancel(
            AlarmItem(
                time = time,
                title = title,
                text = text,
                repeatable = repeatable
            )
        )
    }

    companion object {
        private const val STORE_NAME = "EventListStore"
    }
}
