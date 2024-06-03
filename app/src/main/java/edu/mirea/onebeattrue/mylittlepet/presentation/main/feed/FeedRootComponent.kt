package edu.mirea.onebeattrue.mylittlepet.presentation.main.feed

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.general.FeedComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.petinfo.PetInfoComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.qrcode.QrCodeComponent

interface FeedRootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class Feed(val component: FeedComponent) : Child()
        class QrCode(val component: QrCodeComponent) : Child()
        class PetInfo(val component: PetInfoComponent) : Child()
    }
}