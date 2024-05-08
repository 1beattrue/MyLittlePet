package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.time

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
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.time.EventTimeStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.time.EventTimeStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.time.EventTimeStore.State
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

interface EventTimeStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class ChangeTime(val hours: Int, val minutes: Int) : Intent
        data class ChangePeriod(val isDaily: Boolean) : Intent
        data object GoNext : Intent
    }

    data class State(
        val hours: Int,
        val minutes: Int,
        val isDaily: Boolean
    )

    sealed interface Label {
        data class GoNext(val hours: Int, val minutes: Int) : Label
        data object Finish : Label
    }
}

class EventTimeStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val alarmScheduler: AlarmScheduler,
    private val editPetUseCase: EditPetUseCase
) {

    fun create(
        eventText: String,
        pet: Pet
    ): EventTimeStore =
        object : EventTimeStore, Store<Intent, State, Label> by storeFactory.create(
            name = "EventTimeStore",
            initialState = State(
                hours = 12,
                minutes = 0,
                isDaily = true
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = { ExecutorImpl(eventText, pet) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {
        data class OnTimeChanged(val hours: Int, val minutes: Int) : Msg
        data class OnPeriodChanged(val isDaily: Boolean) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl(
        private val eventText: String,
        private val pet: Pet,
    ) : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.ChangeTime -> {
                    dispatch(
                        Msg.OnTimeChanged(
                            hours = intent.hours,
                            minutes = intent.minutes
                        )
                    )
                }

                Intent.GoNext -> {
                    scope.launch {
                        val isDaily = getState().isDaily
                        val hours = getState().hours
                        val minutes = getState().minutes

                        if (isDaily) {
                            alarmScheduler.schedule(
                                AlarmItem(
                                    title = pet.name,
                                    text = eventText,
                                    time = getTimeMillis(
                                        hours = hours,
                                        minutes = minutes
                                    ),
                                    repeatable = true
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
                        } else {
                            publish(Label.GoNext(hours, minutes))
                        }
                    }
                }

                is Intent.ChangePeriod -> {
                    dispatch(Msg.OnPeriodChanged(intent.isDaily))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.OnPeriodChanged -> copy(isDaily = msg.isDaily)
                is Msg.OnTimeChanged -> copy(
                    hours = msg.hours,
                    minutes = msg.minutes
                )
            }
    }

    private fun getTimeMillis(
        hours: Int,
        minutes: Int
    ): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hours)
            set(Calendar.MINUTE, minutes)
            set(Calendar.SECOND, 0)
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
