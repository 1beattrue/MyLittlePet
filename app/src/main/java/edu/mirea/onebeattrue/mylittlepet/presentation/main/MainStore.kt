package edu.mirea.onebeattrue.mylittlepet.presentation.main

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.mylittlepet.presentation.main.MainStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.MainStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.main.MainStore.State
import javax.inject.Inject

interface MainStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class ChangeBottomMenuVisibility(val visibility: Boolean) : Intent
    }

    data class State(
        val selectedItem: NavigationItem,
        val bottomMenuVisibility: Boolean
    )

    sealed interface Label
}

class MainStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory
) {

    fun create(): MainStore =
        object : MainStore, Store<Intent, State, Label> by storeFactory.create(
            name = "MainStore",
            initialState = State(
                selectedItem = NavigationItem.PetsItem,
                bottomMenuVisibility = true
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

    private sealed interface Msg {
        data class ChangeBottomMenuVisibility(val visibility: Boolean) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.ChangeBottomMenuVisibility -> dispatch(
                    Msg.ChangeBottomMenuVisibility(
                        intent.visibility
                    )
                )
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {

                is Msg.ChangeBottomMenuVisibility -> {
                    copy(bottomMenuVisibility = msg.visibility)
                }
            }
    }
}
