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
import kotlinx.coroutines.launch
import javax.inject.Inject

interface PhoneStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class ChangePhone(val phone: String) : Intent
        data class SendCode(val phone: String, val activity: Activity) : Intent
    }

    data class State(
        val phone: String,
        val isIncorrect: Boolean,
        val isEnabled: Boolean,
        val isLoading: Boolean,
        val isFailure: Boolean,
        val failureMessage: String
    )

    sealed interface Label {
        data object SendCode : Label
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
                isIncorrect = false,
                isEnabled = true,
                isLoading = false,
                isFailure = false,
                failureMessage = ""
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {
        data class ChangePhone(val phone: String) : Msg
        data object Loading : Msg
        data object CodeSent : Msg
        data class CodeSentFailure(val message: String) : Msg
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

                is Intent.SendCode -> {
                    if (isValidPhoneNumber(intent.phone)) {
                        dispatch(Msg.Loading)
                        scope.launch {
                            createUserWithPhoneUseCase(
                                phoneNumber = intent.phone,
                                activity = intent.activity
                            ).collect { result ->
                                when (result) {
                                    is AuthState.Failure -> {
                                        dispatch(Msg.CodeSentFailure(result.exception.message.toString()))
                                    }

                                    AuthState.Success -> {
                                        dispatch(Msg.CodeSent)
                                        publish(Label.SendCode)
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
                        isIncorrect = false
                    )
                }

                Msg.IncorrectPhone -> {
                    copy(isIncorrect = true)
                }

                Msg.CodeSent -> {
                    copy(
                        isLoading = false,
                        isEnabled = true
                    )
                }

                is Msg.CodeSentFailure -> {
                    copy(
                        isLoading = false,
                        isEnabled = true,
                        isFailure = true,
                        failureMessage = msg.message
                    )
                }


                Msg.Loading -> {
                    copy(
                        isLoading = true,
                        isEnabled = false
                    )
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
