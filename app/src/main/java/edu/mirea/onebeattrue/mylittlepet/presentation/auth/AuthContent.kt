package edu.mirea.onebeattrue.mylittlepet.presentation.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.otp.OtpContent
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.phone.PhoneContent

@Composable
fun AuthContent(
    modifier: Modifier = Modifier,
    component: AuthComponent
) {
    Children(
        modifier = modifier,
        stack = component.stack,
        animation = stackAnimation(fade())
    ) {
        when (val instance = it.instance) {
            is AuthComponent.Child.Phone -> {
                PhoneContent(component = instance.component)
            }

            is AuthComponent.Child.Otp -> {
                OtpContent(component = instance.component)
            }
        }
    }
}