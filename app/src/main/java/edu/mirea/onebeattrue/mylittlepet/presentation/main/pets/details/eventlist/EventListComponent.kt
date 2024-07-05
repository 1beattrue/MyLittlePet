package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.eventlist

import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Event
import kotlinx.coroutines.flow.StateFlow

interface EventListComponent {
    val model: StateFlow<EventListStore.State>

    fun onAddEvent()

    fun onDeleteEvent(event: Event)

    fun onBackClicked()

    fun onDeletePastEvents()

    fun syncronize()
}