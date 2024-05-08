package edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase

import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.repository.PetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPetByIdUseCase @Inject constructor(private val repository: PetRepository) {
    suspend operator fun invoke(petId: Int): Flow<Pet> {
        return repository.getPetById(petId)
    }
}