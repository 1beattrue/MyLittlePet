package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.AddEventComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.eventlist.EventListComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.general.DetailsComponent

interface DetailsRootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class Details(val component: DetailsComponent) : Child()
        class AddEvent(val component: AddEventComponent) : Child()
        class EventList(val component: EventListComponent) : Child()
    }

    fun onBackClicked()
}