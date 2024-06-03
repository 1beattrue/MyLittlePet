package edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.qrcode

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultQrCodeComponent @AssistedInject constructor(
    @Assisted("onBackClicked") private val onBackClicked: () -> Unit,
    @Assisted("onQrCodeScanned") private val onQrCodeScanned: (String) -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : QrCodeComponent, ComponentContext by componentContext {

    override fun onCodeScanned(scannedText: String) {
        onQrCodeScanned(scannedText)
    }

    override fun onClickBack() {
        onBackClicked()
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onBackClicked") onBackClicked: () -> Unit,
            @Assisted("onQrCodeScanned") onQrCodeScanned: (String) -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultQrCodeComponent
    }
}