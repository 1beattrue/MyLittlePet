package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.text

import kotlinx.coroutines.flow.StateFlow

interface EventTextComponent {
    val model: StateFlow<EventTextStore.State>

    fun onEventTextChanged(text: String)
    fun next()
}