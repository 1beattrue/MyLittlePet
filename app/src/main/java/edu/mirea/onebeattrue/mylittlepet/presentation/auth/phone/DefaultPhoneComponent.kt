package edu.mirea.onebeattrue.mylittlepet.presentation.auth.phone

import android.app.Activity
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

class DefaultPhoneComponent @AssistedInject constructor(
    private val storeFactory: PhoneStoreFactory,

    @Assisted("onAuthFinished") private val onAuthFinished: () -> Unit,

    @Assisted("onConfirmPhone") private val onConfirmPhone: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : PhoneComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }

    init {
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    PhoneStore.Label.SendCode -> {
                        onConfirmPhone()
                    }

                    PhoneStore.Label.FinishAuth -> {
                        onAuthFinished()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<PhoneStore.State>
        get() = store.stateFlow

    override fun onPhoneChanged(phone: String) {
        store.accept(PhoneStore.Intent.ChangePhone(phone))
    }

    override fun onCodeSent(phone: String, activity: Activity) {
        store.accept(PhoneStore.Intent.SendCode(phone, activity))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onAuthFinished") onAuthFinished: () -> Unit,
            @Assisted("onConfirmPhone") onConfirmPhone: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultPhoneComponent
    }
}