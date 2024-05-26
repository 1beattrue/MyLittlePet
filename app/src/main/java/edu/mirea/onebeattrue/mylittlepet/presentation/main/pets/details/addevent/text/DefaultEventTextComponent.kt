package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.text

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.presentation.extensions.componentScope
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.name.DefaultNameComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultEventTextComponent @AssistedInject constructor(
    private val storeFactory: EventTextStoreFactory,

    @Assisted("onNextClicked") private val onNextClicked: (String) -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : EventTextComponent, ComponentContext by componentContext {
    private val store = instanceKeeper.getStore { storeFactory.create() }

    init {
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    is EventTextStore.Label.GoNext -> onNextClicked(it.text)
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<EventTextStore.State>
        get() = store.stateFlow

    override fun onEventTextChanged(text: String) {
        store.accept(EventTextStore.Intent.ChangeText(text))
    }

    override fun next() {
        store.accept(EventTextStore.Intent.GoNext)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onNextClicked") onNextClicked: (String) -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultEventTextComponent
    }
}