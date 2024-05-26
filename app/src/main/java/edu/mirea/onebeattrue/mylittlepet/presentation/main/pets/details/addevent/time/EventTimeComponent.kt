package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.time

import kotlinx.coroutines.flow.StateFlow

interface EventTimeComponent {
    val model: StateFlow<EventTimeStore.State>

    fun onPeriodChanged(isDaily: Boolean)
    fun next(hours: Int, minutes: Int)
}