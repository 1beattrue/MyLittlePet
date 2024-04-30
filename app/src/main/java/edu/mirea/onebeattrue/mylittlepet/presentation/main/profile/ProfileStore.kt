package edu.mirea.onebeattrue.mylittlepet.presentation.main.profile

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.mylittlepet.domain.auth.usecase.SignOutUseCase
import edu.mirea.onebeattrue.mylittlepet.presentation.main.profile.ProfileStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.profile.ProfileStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.main.profile.ProfileStore.State
import javax.inject.Inject

interface ProfileStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object SignOut : Intent
    }

    data class State(
        val tmp: String
    )

    sealed interface Label {
        data object SignOut : Label
    }
}

class ProfileStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val signOutUseCase: SignOutUseCase,
) {

    fun create(): ProfileStore =
        object : ProfileStore, Store<Intent, State, Label> by storeFactory.create(
            name = "ProfileStore",
            initialState = State(""),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.SignOut -> {
                    signOutUseCase()
                    publish(Label.SignOut)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = copy()
//            when (msg) {
//
//            }
    }
}
