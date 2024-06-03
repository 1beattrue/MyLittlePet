package edu.mirea.onebeattrue.mylittlepet.presentation.main.feed

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.general.FeedContent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.petinfo.PetInfoContent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.qrcode.QrCodeContent

@Composable
fun FeedRootContent(
    modifier: Modifier = Modifier,
    component: FeedRootComponent
) {
    Children(
        modifier = modifier,
        stack = component.stack,
        animation = stackAnimation(fade())
    ) {
        when (val instance = it.instance) {
            is FeedRootComponent.Child.Feed -> FeedContent(component = instance.component)
            is FeedRootComponent.Child.PetInfo -> PetInfoContent(component = instance.component)
            is FeedRootComponent.Child.QrCode -> QrCodeContent(component = instance.component)
        }
    }
}