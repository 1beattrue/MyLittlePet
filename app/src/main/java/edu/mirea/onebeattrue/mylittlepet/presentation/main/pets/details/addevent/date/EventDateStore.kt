package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.date

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
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.date.EventDateStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.date.EventDateStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.date.EventDateStore.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface EventDateStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class Finish(val date: Long) : Intent
    }

    data class State(
        val failure: Boolean,
        val progress: Boolean
    )

    sealed interface Label {
        data object Finish : Label
    }
}

class EventDateStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val addEventUseCase: AddEventUseCase,
    private val alarmScheduler: AlarmScheduler
) {

    fun create(
        eventText: String,
        eventTimeHours: Int,
        eventTimeMinutes: Int,
        pet: Pet
    ): EventDateStore =
        object : EventDateStore, Store<Intent, State, Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = State(
                failure = false,
                progress = false
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = {
                ExecutorImpl(eventText, eventTimeHours, eventTimeMinutes, pet)
            },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {
        data object FailureAddingEvent : Msg
        data object Loading : Msg
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
                is Intent.Finish -> {
                    scope.launch {
                        dispatch(Msg.Loading)

                        val hours = eventTimeHours
                        val minutes = eventTimeMinutes

                        val triggerTime = getTimeMillis(intent.date, hours, minutes)
                        val currentTime = System.currentTimeMillis()

                        val newEvent = Event(
                            time = triggerTime,
                            label = eventText,
                            repeatable = false,
                            petId = pet.id
                        )

                        try {
                            withContext(Dispatchers.IO) {
                                addEventUseCase(newEvent)
                            }

                            if (triggerTime > currentTime) {
                                alarmScheduler.schedule(
                                    AlarmItem(
                                        title = pet.name,
                                        text = newEvent.label,
                                        time = newEvent.time,
                                        repeatable = newEvent.repeatable
                                    )
                                )
                            }

                            publish(Label.Finish)
                        } catch (_: Exception) {
                            dispatch(Msg.FailureAddingEvent)
                        }
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                Msg.FailureAddingEvent -> copy(progress = false, failure = true)
                Msg.Loading -> copy(progress = true, failure = false)
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

    companion object {
        private const val STORE_NAME = "EventDateStore"
    }
}
