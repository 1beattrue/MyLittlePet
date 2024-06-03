package edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.general

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.presentation.utils.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultFeedComponent @AssistedInject constructor(
    private val storeFactory: FeedStoreFactory,

    @Assisted("onOpenPetInfoClicked") private val onOpenPetInfoClicked: (Pet) -> Unit,
    @Assisted("onScanQrCodeClicked") private val onScanQrCodeClicked: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : FeedComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }

    init {
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    is FeedStore.Label.OpenPetInfo -> onOpenPetInfoClicked(it.pet)
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<FeedStore.State>
        get() = store.stateFlow

    override fun openScanner() {
        onScanQrCodeClicked()
    }

    override fun openPetInfo(pet: Pet) {
        store.accept(FeedStore.Intent.OpenPetInfo(pet))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onOpenPetInfoClicked") onOpenPetInfoClicked: (Pet) -> Unit,
            @Assisted("onScanQrCodeClicked") onScanQrCodeClicked: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultFeedComponent
    }
}