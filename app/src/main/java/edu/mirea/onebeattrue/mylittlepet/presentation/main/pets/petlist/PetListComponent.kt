package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.petlist

import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import kotlinx.coroutines.flow.StateFlow

interface PetListComponent {
    val model: StateFlow<PetListStore.State>

    fun addPet()
    fun editPet(pet: Pet)
    fun deletePet(pet: Pet)
    fun openDetails(pet: Pet)
    fun synchronize()
}