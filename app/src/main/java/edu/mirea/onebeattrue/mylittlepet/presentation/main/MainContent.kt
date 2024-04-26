package edu.mirea.onebeattrue.mylittlepet.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.PetsContent

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    component: MainComponent
) {
    Children(
        modifier = modifier,
        stack = component.stack,
        animation = stackAnimation(fade())
    ) {
        when (val instance = it.instance) {
            is MainComponent.Child.Pets -> {
                PetsContent(component = instance.component)
            }
        }
    }
}