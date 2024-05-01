package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.name

import kotlinx.coroutines.flow.StateFlow

interface NameComponent {
    val model: StateFlow<NameStore.State>

    fun setPetName(petName: String)

    fun next()
}