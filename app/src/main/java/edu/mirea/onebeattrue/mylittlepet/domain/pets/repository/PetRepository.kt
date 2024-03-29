package edu.mirea.onebeattrue.mylittlepet.domain.pets.repository

import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import kotlinx.coroutines.flow.Flow

interface PetRepository {
    suspend fun addPet(pet: Pet)
    suspend fun deletePet(pet: Pet)
    suspend fun editPet(pet: Pet)
    fun getPetList(): Flow<List<Pet>>
}