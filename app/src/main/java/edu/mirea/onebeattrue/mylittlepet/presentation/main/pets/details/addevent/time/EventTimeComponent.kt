package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.time

import kotlinx.coroutines.flow.StateFlow

interface EventTimeComponent {
    val model: StateFlow<EventTimeStore.State>

    fun onPeriodChanged(isDaily: Boolean)
    fun onTimeChanged(hours: Int, minutes: Int)
    fun next()
}