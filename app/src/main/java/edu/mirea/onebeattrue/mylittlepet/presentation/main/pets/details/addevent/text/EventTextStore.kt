package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.text

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.text.EventTextStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.text.EventTextStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.text.EventTextStore.State
import javax.inject.Inject

interface EventTextStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class ChangeText(val text: String) : Intent
        data object GoNext : Intent
    }

    data class State(
        val text: String,
        val isIncorrect: Boolean
    )

    sealed interface Label {
        data class GoNext(val text: String) : Label
    }
}

class EventTextStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory
) {
    fun create(): EventTextStore =
        object : EventTextStore, Store<Intent, State, Label> by storeFactory.create(
            name = "EventTextStore",
            initialState = State(
                text = "",
                isIncorrect = false
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {
        data class OnTextChaned(val text: String) : Msg
        data object TextNotEntered : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.ChangeText -> {
                    val text = formattedText(intent.text)
                    dispatch(Msg.OnTextChaned(text))
                }

                Intent.GoNext -> {
                    val text = getState().text
                    if (text.isBlank()) {
                        dispatch(Msg.TextNotEntered)
                    } else {
                        publish(Label.GoNext(text))
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.OnTextChaned -> copy(text = msg.text, isIncorrect = false)
                Msg.TextNotEntered -> copy(isIncorrect = true)
            }
    }

    private fun formattedText(text: String): String {
        if (text.length > 500) return text.substring(0..<500)
        return text
    }
}
