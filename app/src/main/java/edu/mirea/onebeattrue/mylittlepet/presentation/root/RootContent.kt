package edu.mirea.onebeattrue.mylittlepet.presentation.root

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.AuthContent
import edu.mirea.onebeattrue.mylittlepet.presentation.extensions.LocaleUtils
import edu.mirea.onebeattrue.mylittlepet.presentation.main.MainContent
import edu.mirea.onebeattrue.mylittlepet.ui.theme.MyLittlePetTheme

@SuppressLint("RememberReturnType")
@Composable
fun RootContent(
    modifier: Modifier = Modifier,
    component: RootComponent
) {
    val state by component.model.collectAsState()
    val context = LocalContext.current

    SideEffect {
        LocaleUtils.setLocale(context, state.isEnglishLanguage)
    }


    MyLittlePetTheme(
        darkTheme = state.isDarkTheme ?: isSystemInDarkTheme()
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

}


