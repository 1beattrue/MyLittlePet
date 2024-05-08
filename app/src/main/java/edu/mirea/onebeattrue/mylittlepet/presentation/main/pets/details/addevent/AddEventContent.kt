package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.date.EventDateContent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.text.EventTextContent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.time.EventTimeContent

@Composable
fun AddEventContent(
    modifier: Modifier = Modifier,
    component: AddEventComponent
) {
    Children(
        modifier = modifier,
        stack = component.stack,
        animation = stackAnimation(slide() + fade())
    ) {
        when (val instance = it.instance) {
            is AddEventComponent.Child.Date -> EventDateContent(component = instance.component)
            is AddEventComponent.Child.Text -> EventTextContent(component = instance.component)
            is AddEventComponent.Child.Time -> EventTimeContent(component = instance.component)
        }
    }
}