package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.time

import android.app.AlarmManager
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.AlarmItem
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.AlarmScheduler
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Event
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.AddEventUseCase
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.time.EventTimeStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.time.EventTimeStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.time.EventTimeStore.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

interface EventTimeStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class ChangePeriod(val isDaily: Boolean) : Intent
        data class GoNext(val hours: Int, val minutes: Int) : Intent
    }

    data class State(
        val isDaily: Boolean,
        val failure: Boolean,
        val progress: Boolean
    )

    sealed interface Label {
        data class GoNext(val hours: Int, val minutes: Int) : Label
        data object Finish : Label
    }
}

class EventTimeStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val alarmScheduler: AlarmScheduler,
    private val addEventUseCase: AddEventUseCase
) {

    fun create(
        eventText: String,
        pet: Pet,
    ): EventTimeStore =
        object : EventTimeStore, Store<Intent, State, Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = State(
                isDaily = true,
                failure = false,
                progress = false
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = { ExecutorImpl(eventText, pet) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {
        data class OnPeriodChanged(val isDaily: Boolean) : Msg
        data object FailureAddingEvent : Msg
        data object Loading : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl(
        private val eventText: String,
        private val pet: Pet
    ) : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.GoNext -> {
                    scope.launch {
                        val isDaily = getState().isDaily

                        val hours = intent.hours
                        val minutes = intent.minutes

                        var triggerTime = getTimeMillis(intent.hours, intent.minutes)
                        val currentTime = System.currentTimeMillis()
                        if (triggerTime <= currentTime) {
                            triggerTime += AlarmManager.INTERVAL_DAY
                        }

                        if (isDaily) {
                            dispatch(Msg.Loading)

                            val newEvent = Event(
                                time = triggerTime,
                                label = eventText,
                                repeatable = true,
                                petId = pet.id
                            )
                            try {
                                withContext(Dispatchers.IO) {
                                    addEventUseCase(newEvent)
                                }

                                alarmScheduler.schedule(
                                    AlarmItem(
                                        title = pet.name,
                                        text = newEvent.label,
                                        time = newEvent.time,
                                        repeatable = newEvent.repeatable
                                    )
                                )

                                publish(Label.Finish)
                            } catch (_: Exception) {
                                dispatch(Msg.FailureAddingEvent)
                            }
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
                Msg.FailureAddingEvent -> copy(progress = false, failure = true)
                Msg.Loading -> copy(progress = true, failure = false)
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

    companion object {
        private const val STORE_NAME = "EventTimeStore"
    }
}
