package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.AddEventContent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addmedicaldata.AddMedicalDataContent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addnote.AddNoteContent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.eventlist.EventListContent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.general.DetailsContent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.medicaldatalist.MedicalDataListContent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.medicaldatadetails.MedicalDataDetailsContent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.notelist.NoteListContent

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
            is DetailsRootComponent.Child.EventList -> EventListContent(component = instance.component)
            is DetailsRootComponent.Child.NoteList -> NoteListContent(component = instance.component)
            is DetailsRootComponent.Child.AddNote -> AddNoteContent(component = instance.component)
            is DetailsRootComponent.Child.MedicalDataList -> MedicalDataListContent(component = instance.component)
            is DetailsRootComponent.Child.AddMedicalData -> AddMedicalDataContent(component = instance.component)
            is DetailsRootComponent.Child.MedicalDataDetails -> MedicalDataDetailsContent(component = instance.component)
        }
    }
}