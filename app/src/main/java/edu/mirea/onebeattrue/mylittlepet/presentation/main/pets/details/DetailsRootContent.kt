package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.AddEventContent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.general.DetailsContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsRootContent(
    modifier: Modifier = Modifier,
    component: DetailsRootComponent
) {
    Children(
        modifier = modifier,
        stack = component.stack,
        animation = stackAnimation(fade())
    ) {
        when (val instance = it.instance) {
            is DetailsRootComponent.Child.AddEvent -> AddEventContent(component = instance.component)
            is DetailsRootComponent.Child.Details -> DetailsContent(component = instance.component)
        }
    }
}