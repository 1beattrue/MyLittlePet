package edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.petinfo

import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import kotlinx.coroutines.flow.StateFlow


interface PetInfoComponent {
    val pet: Pet?
    val state: StateFlow<PetInfoStore.State>

    fun onClickBack()

    fun addPet()
}
