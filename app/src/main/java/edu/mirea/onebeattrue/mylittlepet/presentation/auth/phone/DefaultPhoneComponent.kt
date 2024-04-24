package edu.mirea.onebeattrue.mylittlepet.presentation.auth.phone

import android.app.Activity
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.presentation.extensions.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultPhoneComponent @AssistedInject constructor(
    private val storeFactory: PhoneStoreFactory,

    @Assisted("confirmPhone") private val confirmPhone: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : PhoneComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }

    init {
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    PhoneStore.Label.ConfirmPhone -> {
                        confirmPhone()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<PhoneStore.State>
        get() = store.stateFlow

    override fun changePhone(phone: String) {
        store.accept(PhoneStore.Intent.ChangePhone(phone))
    }

    override fun onConfirmPhone(phone: String, activity: Activity) {
        store.accept(PhoneStore.Intent.ConfirmPhone(phone, activity))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("confirmPhone") confirmPhone: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultPhoneComponent
    }
}