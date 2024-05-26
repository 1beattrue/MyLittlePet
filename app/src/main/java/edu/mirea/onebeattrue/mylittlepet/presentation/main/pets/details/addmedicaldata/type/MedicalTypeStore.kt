package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addmedicaldata.type

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalDataType
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addmedicaldata.type.TypeStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addmedicaldata.type.TypeStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addmedicaldata.type.TypeStore.State
import javax.inject.Inject

interface TypeStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class SetType(val medicalDataType: MedicalDataType) : Intent
        data object Next : Intent
        data class ChangeDropdownMenuExpanded(val expanded: Boolean) : Intent
    }

    data class State(
        val medicalDataType: MedicalDataType?,
        val isIncorrect: Boolean,
        val expanded: Boolean
    )

    sealed interface Label {
        data class Next(val medicalDataType: MedicalDataType) : Label
    }
}

class TypeStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory
) {

    fun create(): TypeStore =
        object : TypeStore, Store<Intent, State, Label> by storeFactory.create(
            name = "TypeStore",
            initialState = State(
                medicalDataType = null,
                isIncorrect = false,
                expanded = false
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {
        data class SetType(val medicalDataType: MedicalDataType) : Msg
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
                    val medicalDataType = getState().medicalDataType
                    if (medicalDataType == null) {
                        dispatch(Msg.TypeNotSelected)
                    } else {
                        publish(Label.Next(medicalDataType))
                    }
                }

                is Intent.ChangeDropdownMenuExpanded -> {
                    dispatch(Msg.ChangeDropdownMenuExpanded(intent.expanded))
                }

                is Intent.SetType -> {
                    dispatch(Msg.SetType(intent.medicalDataType))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                Msg.TypeNotSelected -> copy(isIncorrect = true)
                is Msg.ChangeDropdownMenuExpanded -> copy(expanded = msg.expanded)
                is Msg.SetType -> copy(medicalDataType = msg.medicalDataType, isIncorrect = false)
            }
    }
}