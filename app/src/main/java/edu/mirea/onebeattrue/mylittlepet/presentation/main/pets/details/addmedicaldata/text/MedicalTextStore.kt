package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addmedicaldata.text

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addmedicaldata.text.MedicalTextStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addmedicaldata.text.MedicalTextStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addmedicaldata.text.MedicalTextStore.State
import javax.inject.Inject

interface MedicalTextStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class SetText(val text: String) : Intent
        data object Next : Intent
    }

    data class State(
        val text: String,
        val isIncorrect: Boolean
    )

    sealed interface Label {
        data class Next(val text: String) : Label
    }
}

class MedicalTextStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory
) {

    fun create(): MedicalTextStore =
        object : MedicalTextStore, Store<Intent, State, Label> by storeFactory.create(
            name = "MedicalTextStore",
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
        data class SetText(val text: String) : Msg
        data object TextNotEntered : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.Next -> {
                    val text = getState().text
                    if (text.isBlank()) {
                        dispatch(Msg.TextNotEntered)
                    } else {
                        publish(Label.Next(text))
                    }
                }

                is Intent.SetText -> {
                    dispatch(Msg.SetText(intent.text))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                Msg.TextNotEntered -> copy(isIncorrect = true)
                is Msg.SetText -> copy(isIncorrect = false, text = msg.text)
            }
    }
}
