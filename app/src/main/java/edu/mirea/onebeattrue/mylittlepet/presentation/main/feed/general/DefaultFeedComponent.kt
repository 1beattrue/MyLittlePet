package edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.general

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.presentation.main.profile.ProfileStoreFactory

class DefaultFeedComponent @AssistedInject constructor(
    @Assisted("onScanQrCodeClicked") private val onScanQrCodeClicked: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : FeedComponent, ComponentContext by componentContext {


    override fun openScanner() {
        onScanQrCodeClicked()
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onScanQrCodeClicked") onScanQrCodeClicked: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultFeedComponent
    }
}