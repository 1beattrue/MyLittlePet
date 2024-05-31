package edu.mirea.onebeattrue.mylittlepet.presentation.auth.otp

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.presentation.utils.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultOtpComponent @AssistedInject constructor(
    private val storeFactory: OtpStoreFactory,

    @Assisted("onAuthFinished") private val onAuthFinished: () -> Unit,

    @Assisted("onClickBack") private val onClickBack: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : OtpComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }

    init {
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    OtpStore.Label.ClickBack -> {
                        onClickBack()
                    }

                    OtpStore.Label.FinishAuth -> {
                        onAuthFinished()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<OtpStore.State>
        get() = store.stateFlow

    override fun onOtpChanged(otp: String) {
        store.accept(OtpStore.Intent.ChangeOtp(otp))
    }

    override fun onConfirmPhone(otp: String) {
        store.accept(OtpStore.Intent.ConfirmPhone(otp))
    }

    override fun onResendClicked() {
        store.accept(OtpStore.Intent.ResendOtp)
    }

    override fun onBackClicked() {
        store.accept(OtpStore.Intent.ClickBack)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onAuthFinished") onAuthFinished: () -> Unit,
            @Assisted("onClickBack") onClickBack: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultOtpComponent
    }
}