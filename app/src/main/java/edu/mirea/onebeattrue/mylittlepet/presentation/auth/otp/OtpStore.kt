package edu.mirea.onebeattrue.mylittlepet.presentation.auth.otp

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.mylittlepet.domain.auth.entity.AuthState
import edu.mirea.onebeattrue.mylittlepet.domain.auth.usecase.IsLoggedInUseCase
import edu.mirea.onebeattrue.mylittlepet.domain.auth.usecase.ResendVerificationCodeUseCase
import edu.mirea.onebeattrue.mylittlepet.domain.auth.usecase.SignInWithCredentialUseCase
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.otp.OtpStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.otp.OtpStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.otp.OtpStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface OtpStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class ChangeOtp(val otp: String) : Intent
        data class ConfirmPhone(val otp: String) : Intent
        data object ResendOtp : Intent
        data object ClickBack : Intent
    }

    data class State(
        val otp: String,
        val isIncorrect: Boolean,
        val isEnabled: Boolean,
        val isLoading: Boolean,
        val isFailure: Boolean,
        val failureMessage: String,
        val wasResent: Boolean
    )

    sealed interface Label {
        data object FinishAuth : Label
        data object ClickBack : Label
    }
}

class OtpStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val signInWithCredentialUseCase: SignInWithCredentialUseCase,
    private val resendVerificationCodeUseCase: ResendVerificationCodeUseCase,
    private val isLoggedInUseCase: IsLoggedInUseCase
) {
    fun create(): OtpStore =
        object : OtpStore, Store<Intent, State, Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = State(
                otp = "",
                isIncorrect = false,
                isEnabled = true,
                isLoading = false,
                isFailure = false,
                failureMessage = "",
                wasResent = false
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data object LoggedIn : Action
    }

    private sealed interface Msg {
        data class OtpChanged(val otp: String) : Msg
        data object Loading : Msg
        data object PhoneConfirmed : Msg
        data class Failure(val message: String) : Msg
        data object IncorrectOtp : Msg
        data object OtpResent : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                isLoggedInUseCase().collect { isLoggedIn ->
                    if (isLoggedIn) {
                        dispatch(Action.LoggedIn)
                    }
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.ChangeOtp -> {
                    val code = intent.otp
                    if (isCorrectInput(code)) {
                        dispatch(Msg.OtpChanged(formattedCode(code)))
                    }
                }

                is Intent.ConfirmPhone -> {
                    if (isValidConfirmationCode(intent.otp)) {
                        dispatch(Msg.Loading)
                        scope.launch {
                            signInWithCredentialUseCase(
                                code = intent.otp
                            ).collect { result ->
                                when (result) {
                                    is AuthState.Failure -> {
                                        dispatch(Msg.Failure(result.exception.message.toString()))
                                    }

                                    AuthState.Success -> {
                                        dispatch(Msg.PhoneConfirmed)
                                        publish(Label.FinishAuth)
                                    }
                                }

                            }
                        }
                    } else {
                        dispatch(Msg.IncorrectOtp)
                    }
                }

                Intent.ClickBack -> {
                    publish(Label.ClickBack)
                }

                Intent.ResendOtp -> {
                    dispatch(Msg.Loading)
                    scope.launch {
                        resendVerificationCodeUseCase().collect { result ->
                            when (result) {
                                is AuthState.Failure -> {
                                    dispatch(Msg.Failure(result.exception.message.toString()))
                                }

                                AuthState.Success -> {
                                    dispatch(Msg.OtpResent)
                                }
                            }
                        }
                    }
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                Action.LoggedIn -> {
                    publish(Label.FinishAuth)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.OtpChanged -> {
                    copy(
                        otp = msg.otp,
                        isIncorrect = false,
                        isEnabled = true,
                        isLoading = false,
                        isFailure = false,
                        failureMessage = "",
                        wasResent = false
                    )
                }

                Msg.IncorrectOtp -> {
                    copy(
                        isIncorrect = true,
                        isEnabled = true,
                        isLoading = false,
                        isFailure = false,
                        failureMessage = "",
                        wasResent = false
                    )
                }

                is Msg.Failure -> {
                    copy(
                        isIncorrect = false,
                        isEnabled = true,
                        isLoading = false,
                        isFailure = true,
                        failureMessage = msg.message,
                        wasResent = false
                    )
                }

                Msg.Loading -> {
                    copy(
                        isIncorrect = false,
                        isEnabled = false,
                        isLoading = true,
                        isFailure = false,
                        failureMessage = "",
                        wasResent = false
                    )
                }

                Msg.OtpResent -> {
                    copy(
                        isIncorrect = false,
                        isEnabled = true,
                        isLoading = false,
                        isFailure = false,
                        failureMessage = "",
                        wasResent = true
                    )
                }

                Msg.PhoneConfirmed -> {
                    copy(
                        isIncorrect = false,
                        isEnabled = true,
                        isLoading = false,
                        isFailure = false,
                        failureMessage = "",
                        wasResent = false
                    )
                }
            }
    }

    private fun formattedCode(code: String): String {
        if (code.length > 6) return code.substring(0..5)
        return code
    }

    private fun isCorrectInput(input: String): Boolean {
        return input.all { it.isDigit() }
    }

    private fun isValidConfirmationCode(code: String): Boolean {
        return code.trim().length == 6
    }

    companion object {
        const val STORE_NAME = "OtpStore"
    }
}