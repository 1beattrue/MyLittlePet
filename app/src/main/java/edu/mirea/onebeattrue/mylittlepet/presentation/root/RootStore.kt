package edu.mirea.onebeattrue.mylittlepet.presentation.root

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.mylittlepet.presentation.root.RootStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.root.RootStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.root.RootStore.State
import javax.inject.Inject

interface RootStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class ChangeTheme(val isDarkTheme: Boolean) : Intent
        data class ChangeLanguage(val isEnglishLanguage: Boolean) : Intent
    }

    data class State(
        var isDarkTheme: Boolean?,
        var isEnglishLanguage: Boolean?
    )

    sealed interface Label {
    }
}

class RootStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory
) {

    fun create(
        isDarkTheme: Boolean?
    ): RootStore =
        object : RootStore, Store<Intent, State, Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = State(
                isDarkTheme = isDarkTheme,
                isEnglishLanguage = null
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

    private sealed interface Msg {
        data class ChangeTheme(val isDarkTheme: Boolean) : Msg
        data class ChangeLanguage(val isEnglishLanguage: Boolean) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.ChangeTheme -> {
                    dispatch(Msg.ChangeTheme(intent.isDarkTheme))
                }

                is Intent.ChangeLanguage -> {
                    dispatch(Msg.ChangeLanguage(intent.isEnglishLanguage))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.ChangeTheme -> copy(isDarkTheme = msg.isDarkTheme)
                is Msg.ChangeLanguage -> copy(isEnglishLanguage = msg.isEnglishLanguage)
            }
    }

    companion object {
        const val STORE_NAME = "RootStore"
    }
}