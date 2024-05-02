package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.name

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.name.NameStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.name.NameStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.name.NameStore.State
import javax.inject.Inject

interface NameStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class SetPetName(val petName: String) : Intent
        data object Next : Intent
    }

    data class State(
        val petName: String,
        val isIncorrect: Boolean
    )

    sealed interface Label {
        data class Next(val petName: String) : Label
    }
}

class NameStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory
) {

    fun create(): NameStore =
        object : NameStore, Store<Intent, State, Label> by storeFactory.create(
            name = "NameStore",
            initialState = State(
                petName = "",
                isIncorrect = false
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {
        data class SetPetName(val petName: String) : Msg
        data object NameNotEntered : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.Next -> {
                    val petName = getState().petName
                    if (petName.isBlank()) {
                        dispatch(Msg.NameNotEntered)
                    } else {
                        publish(Label.Next(petName))
                    }
                }

                is Intent.SetPetName -> {
                    dispatch(Msg.SetPetName(formattedName(intent.petName)))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                Msg.NameNotEntered -> copy(isIncorrect = true)
                is Msg.SetPetName -> copy(isIncorrect = false, petName = msg.petName)
            }
    }

    private fun formattedName(name: String): String {
        if (name.length > 50) return name.substring(0..<50)
        return name
    }
}
