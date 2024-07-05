package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.date

import kotlinx.coroutines.flow.StateFlow

interface EventDateComponent {
    val model: StateFlow<EventDateStore.State>

    fun finish(dateMillis: Long)
}