package edu.mirea.onebeattrue.mylittlepet.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.otp.OtpContent
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.phone.PhoneContent

@Composable
fun AuthContent(
    modifier: Modifier = Modifier,
    component: AuthComponent
) {
    //TopAppBar(title = {})
    Children(
        modifier = modifier.background(MaterialTheme.colorScheme.surface),
        stack = component.stack,
        animation = stackAnimation(slide() + fade())
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