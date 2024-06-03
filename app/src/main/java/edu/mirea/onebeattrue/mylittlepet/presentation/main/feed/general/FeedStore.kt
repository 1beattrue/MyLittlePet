package edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.general

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.GetLastScannedPetUseCase
import edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.general.FeedStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.general.FeedStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.general.FeedStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface FeedStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class OpenPetInfo(val pet: Pet) : Intent
    }

    data class State(
        val lastPetScanned: Pet?
    )

    sealed interface Label {
        data class OpenPetInfo(val pet: Pet) : Label
    }
}

class FeedStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getLastScannedPetUseCase: GetLastScannedPetUseCase
) {

    fun create(): FeedStore =
        object : FeedStore, Store<Intent, State, Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = State(
                lastPetScanned = null
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class UpdateLastPetScanned(val pet: Pet?) : Action
    }

    private sealed interface Msg {
        data class UpdateLastPetScanned(val pet: Pet?) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getLastScannedPetUseCase().collect {
                    dispatch(Action.UpdateLastPetScanned(it))
                }
            }
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.UpdateLastPetScanned -> dispatch(Msg.UpdateLastPetScanned(action.pet))
            }
        }

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.OpenPetInfo -> publish(Label.OpenPetInfo(intent.pet))
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.UpdateLastPetScanned -> copy(lastPetScanned = msg.pet)
            }
    }

    companion object {
        private const val STORE_NAME = "FeedStore"
    }
}
