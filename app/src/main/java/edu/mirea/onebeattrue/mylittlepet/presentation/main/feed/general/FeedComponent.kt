package edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.general

import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import kotlinx.coroutines.flow.StateFlow

interface FeedComponent {

    val state: StateFlow<FeedStore.State>

    fun openScanner()
    fun openPetInfo(pet: Pet)
}