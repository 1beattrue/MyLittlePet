package edu.mirea.onebeattrue.mylittlepet.presentation.root

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.AuthContent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.MainContent

@Composable
fun RootContent(
    modifier: Modifier = Modifier,
    component: RootComponent
) {
    Children(
        modifier = modifier,
        stack = component.stack,
        animation = stackAnimation(fade())
    ) {
        when (val instance = it.instance) {
            is RootComponent.Child.Auth -> AuthContent(component = instance.component)
            is RootComponent.Child.Main -> MainContent(component = instance.component)
        }
    }
}