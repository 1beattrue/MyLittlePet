package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.type

import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.PetType
import kotlinx.coroutines.flow.StateFlow

interface TypeComponent {
    val model: StateFlow<TypeStore.State>

    fun setPetType(petType: PetType)

    fun next()

    fun openDropdownMenu()
    fun closeDropdownMenu()
}