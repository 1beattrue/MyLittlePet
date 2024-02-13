package edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase

import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.repository.PetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPetListUseCase @Inject constructor(private val repository: PetRepository) {
    operator fun invoke(): Flow<List<Pet>> {
        return repository.getPetList()
    }
}