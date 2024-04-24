package edu.mirea.onebeattrue.mylittlepet.presentation.auth.otp

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.mylittlepet.domain.auth.entity.AuthState
import edu.mirea.onebeattrue.mylittlepet.domain.auth.usecase.ResendVerificationCodeUseCase
import edu.mirea.onebeattrue.mylittlepet.domain.auth.usecase.SignInWithCredentialUseCase
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.otp.OtpStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.otp.OtpStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.otp.OtpStore.State
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

interface OtpStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class ChangeOtp(val otp: String) : Intent
        data class ConfirmOtp(val otp: String) : Intent
        data object ResendOtp : Intent
        data object ClickBack : Intent
    }

    data class State(
        val otp: String,
        val isIncorrectOtp: Boolean,
        val otpState: OtpState
    ) {
        sealed interface OtpState {
            data object Initial : OtpState
            data object Loading : OtpState
            data class Error(
                val message: String
            ) : OtpState

            data object Resent : OtpState
        }
    }

    sealed interface Label {
        data object ConfirmOtp : Label
        data object ClickBack : Label
    }
}

class OtpStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val signInWithCredentialUseCase: SignInWithCredentialUseCase,
    private val resendVerificationCodeUseCase: ResendVerificationCodeUseCase
) {
    fun create(): OtpStore =
        object : OtpStore, Store<Intent, State, Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = State(
                otp = "",
                isIncorrectOtp = false,
                otpState = State.OtpState.Initial
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {
        data class ChangeOtp(val otp: String) : Msg
        data object Loading : Msg
        data class Error(val message: String) : Msg
        data object IncorrectOtp : Msg
        data object ResendOtp : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.ChangeOtp -> {
                    dispatch(Msg.ChangeOtp(intent.otp))
                }

                is Intent.ConfirmOtp -> {
                    if (isValidConfirmationCode(intent.otp)) {
                        dispatch(Msg.Loading)
                        scope.launch {

                            signInWithCredentialUseCase(
                                code = intent.otp
                            ).collect { result ->
                                when (result) {
                                    is AuthState.Failure -> {
                                        dispatch(Msg.Error(result.exception.message.toString()))
                                    }

                                    AuthState.Success -> {
                                        publish(Label.ConfirmOtp)
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
                                    dispatch(Msg.Error(result.exception.message.toString()))
                                }

                                AuthState.Success -> {
                                    dispatch(Msg.ResendOtp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.ChangeOtp -> {
                    copy(
                        otp = msg.otp,
                        isIncorrectOtp = false,
                        otpState = State.OtpState.Initial
                    )
                }

                is Msg.Error -> {
                    copy(otpState = State.OtpState.Error(msg.message))
                }

                Msg.IncorrectOtp -> {
                    copy(isIncorrectOtp = true)
                }

                Msg.Loading -> {
                    copy(otpState = State.OtpState.Loading)
                }

                Msg.ResendOtp -> {
                    copy(otpState = State.OtpState.Resent)
                }
            }
    }

    private fun isValidConfirmationCode(code: String): Boolean {
        return code.trim().length == 6
    }

    companion object {
        const val STORE_NAME = "OtpStore"
    }
}