package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.AddEventComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.eventlist.EventListComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.general.DetailsComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.notelist.NoteListComponent

interface DetailsRootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class Details(val component: DetailsComponent) : Child()
        class AddEvent(val component: AddEventComponent) : Child()
        class EventList(val component: EventListComponent) : Child()
        class NoteList(val component: NoteListComponent) : Child()
    }

    fun onBackClicked()
}