package edu.mirea.onebeattrue.mylittlepet.presentation.root

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.AuthContent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.MainContent
import edu.mirea.onebeattrue.mylittlepet.presentation.onboarding.OnboardingContent
import edu.mirea.onebeattrue.mylittlepet.presentation.utils.LocaleUtils
import edu.mirea.onebeattrue.mylittlepet.ui.theme.MyLittlePetTheme

@Composable
fun RootContent(
    modifier: Modifier = Modifier,
    component: RootComponent
) {
    val state by component.model.collectAsState()
    val context = LocalContext.current

    LocaleUtils.setLocale(context, state.isEnglishLanguage)

    when (state.isEnglishLanguage) { // TODO: костыль для перерисовки при изменении языка
        true -> {
            StatefulRootContent(modifier = modifier, component = component, state = state)
        }

        false -> {
            StatefulRootContent(modifier = modifier, component = component, state = state)
        }
    }

}

@Composable
private fun StatefulRootContent(
    modifier: Modifier = Modifier,
    component: RootComponent,
    state: RootStore.State
) {
    MyLittlePetTheme(
        darkTheme = state.isDarkTheme ?: isSystemInDarkTheme()
    ) {
        Children(
            modifier = modifier.background(MaterialTheme.colorScheme.surface),
            stack = component.stack,
            animation = stackAnimation(fade())
        ) {
            when (val instance = it.instance) {
                is RootComponent.Child.Auth -> AuthContent(component = instance.component)
                is RootComponent.Child.Main -> MainContent(component = instance.component)
                is RootComponent.Child.Onboarding -> OnboardingContent(component = instance.component)
            }
        }
    }
}


