package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.type

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.PetType
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.type.TypeStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.type.TypeStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.type.TypeStore.State
import javax.inject.Inject

interface TypeStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class SetPetType(val petType: PetType) : Intent
        data object Next : Intent
        data class ChangeDropdownMenuExpanded(val expanded: Boolean) : Intent
    }

    data class State(
        val petType: PetType?,
        val isIncorrect: Boolean,
        val expanded: Boolean
    )

    sealed interface Label {
        data class Next(val petType: PetType) : Label
    }
}

class TypeStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory
) {

    fun create(): TypeStore =
        object : TypeStore, Store<Intent, State, Label> by storeFactory.create(
            name = "AddPetStore",
            initialState = State(
                petType = null,
                isIncorrect = false,
                expanded = false
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {
        data class SetPetType(val petType: PetType) : Msg
        data object TypeNotSelected : Msg
        data class ChangeDropdownMenuExpanded(val expanded: Boolean) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.Next -> {
                    val petType = getState().petType
                    if (petType == null) {
                        dispatch(Msg.TypeNotSelected)
                    } else {
                        publish(Label.Next(petType))
                    }
                }

                is Intent.SetPetType -> {
                    dispatch(Msg.SetPetType(intent.petType))
                }

                is Intent.ChangeDropdownMenuExpanded -> {
                    dispatch(Msg.ChangeDropdownMenuExpanded(intent.expanded))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.SetPetType -> copy(petType = msg.petType, isIncorrect = false)
                Msg.TypeNotSelected -> copy(isIncorrect = true)
                is Msg.ChangeDropdownMenuExpanded -> copy(expanded = msg.expanded)
            }
    }
}