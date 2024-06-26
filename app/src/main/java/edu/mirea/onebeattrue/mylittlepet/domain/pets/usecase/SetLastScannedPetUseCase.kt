package edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase

import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.repository.PetRepository
import javax.inject.Inject

class SetLastScannedPetUseCase @Inject constructor(private val repository: PetRepository) {
    suspend operator fun invoke(pet: Pet) {
        repository.setLastPetScanned(pet)
    }
}