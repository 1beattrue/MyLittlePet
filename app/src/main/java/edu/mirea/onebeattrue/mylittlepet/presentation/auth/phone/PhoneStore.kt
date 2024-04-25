package edu.mirea.onebeattrue.mylittlepet.presentation.auth.phone

import android.app.Activity
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.mylittlepet.domain.auth.entity.AuthState
import edu.mirea.onebeattrue.mylittlepet.domain.auth.usecase.CreateUserWithPhoneUseCase
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.phone.PhoneStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.phone.PhoneStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.phone.PhoneStore.State
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

interface PhoneStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class ChangePhone(val phone: String) : Intent
        data class ConfirmPhone(val phone: String, val activity: Activity) : Intent
    }

    data class State(
        val phone: String,
        val isIncorrectPhone: Boolean,
        val phoneState: PhoneState
    ) {
        sealed interface PhoneState {
            data object Initial : PhoneState
            data object Loading : PhoneState
            data class Error(
                val message: String
            ) : PhoneState
        }
    }

    sealed interface Label {
        data object ConfirmPhone : Label
    }
}

class PhoneStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val createUserWithPhoneUseCase: CreateUserWithPhoneUseCase
) {
    fun create(): PhoneStore =
        object : PhoneStore, Store<Intent, State, Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = State(
                phone = "",
                isIncorrectPhone = false,
                phoneState = State.PhoneState.Initial
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {
        data class ChangePhone(val phone: String) : Msg
        data object Loading : Msg
        data class Error(val message: String) : Msg
        data object IncorrectPhone : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.ChangePhone -> {
                    dispatch(Msg.ChangePhone(intent.phone))
                }

                is Intent.ConfirmPhone -> {
                    if (isValidPhoneNumber(intent.phone)) {
                        dispatch(Msg.Loading)
                        scope.launch {
                            createUserWithPhoneUseCase(
                                phoneNumber = intent.phone,
                                activity = intent.activity
                            ).collect { result ->
                                when (result) {
                                    is AuthState.Failure -> {
                                        dispatch(Msg.Error(result.exception.message.toString()))
                                    }

                                    AuthState.Success -> {
                                        publish(Label.ConfirmPhone)
                                    }
                                }
                            }
                        }
                    } else {
                        dispatch(Msg.IncorrectPhone)
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.ChangePhone -> {
                    copy(
                        phone = msg.phone,
                        isIncorrectPhone = false,
                        phoneState = State.PhoneState.Initial
                    )
                }

                is Msg.Error -> {
                    copy(phoneState = State.PhoneState.Error(msg.message))
                }

                Msg.IncorrectPhone -> {
                    copy(isIncorrectPhone = true)
                }

                Msg.Loading -> {
                    copy(phoneState = State.PhoneState.Loading)
                }
            }
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return phoneNumber.trim().length == 10
    }

    companion object {
        const val STORE_NAME = "PhoneStore"
    }
}