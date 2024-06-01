package edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.petinfo

import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet


interface PetInfoComponent {
    val pet: Pet?

    fun onClickBack()
}
