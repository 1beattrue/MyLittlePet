package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.date

import android.icu.util.Calendar
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
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.date.EventDateStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.date.EventDateStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.date.EventDateStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface EventDateStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class ChangeDate(val date: Long) : Intent
        data object Finish : Intent
    }

    data class State(
        val date: Long
    )

    sealed interface Label {
        data object Finish : Label
    }
}

class EventDateStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val editPetUseCase: EditPetUseCase,
    private val alarmScheduler: AlarmScheduler
) {

    fun create(
        eventText: String,
        eventTimeHours: Int,
        eventTimeMinutes: Int,
        pet: Pet
    ): EventDateStore =
        object : EventDateStore, Store<Intent, State, Label> by storeFactory.create(
            name = "EventDateStore",
            initialState = State(
                date = Calendar.getInstance().timeInMillis
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = {
                ExecutorImpl(eventText, eventTimeHours, eventTimeMinutes, pet)
            },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {
        data class OnDateChanged(val date: Long) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl(
        private val eventText: String,
        private val eventTimeHours: Int,
        private val eventTimeMinutes: Int,
        private val pet: Pet
    ) : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.ChangeDate -> {
                    dispatch(Msg.OnDateChanged(intent.date))
                }

                Intent.Finish -> {
                    scope.launch {
                        val hours = eventTimeHours
                        val minutes = eventTimeMinutes

                        alarmScheduler.schedule(
                            AlarmItem(
                                title = pet.name,
                                text = eventText,
                                time = getTimeMillis(
                                    date = getState().date,
                                    hours = hours,
                                    minutes = minutes
                                ),
                                repeatable = false
                            )
                        )

                        val oldEventList = pet.eventList
                        val newEventList = oldEventList
                            .toMutableList()
                            .apply {
                                add(
                                    Event(
                                        date = null,
                                        hours = hours,
                                        minutes = minutes,
                                        label = eventText,
                                        id = generateEventId(this)
                                    )
                                )
                            }
                            .toList()
                        editPetUseCase(pet = pet.copy(eventList = newEventList))
                        publish(Label.Finish)
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.OnDateChanged -> copy(date = msg.date)
            }
    }

    private fun getTimeMillis(
        date: Long,
        hours: Int,
        minutes: Int
    ): Long {
        val calendar = java.util.Calendar.getInstance().apply {
            timeInMillis = date
            set(java.util.Calendar.HOUR_OF_DAY, hours)
            set(java.util.Calendar.MINUTE, minutes)
            set(java.util.Calendar.SECOND, 0)
        }

        return calendar.timeInMillis
    }

    private fun generateEventId(list: List<Event>): Int {
        if (list.isEmpty()) return 0
        var maxId = list[0].id
        list.forEach {
            if (it.id > maxId) maxId = it.id
        }
        return maxId + 1
    }
}
