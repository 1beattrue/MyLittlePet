package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.date.EventDateComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.text.EventTextComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.time.EventTimeComponent

interface AddEventComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class Text(val component: EventTextComponent) : Child()
        class Time(val component: EventTimeComponent) : Child()
        class Date(val component: EventDateComponent) : Child()
    }
}