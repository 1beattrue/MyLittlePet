package edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase

import edu.mirea.onebeattrue.mylittlepet.domain.pets.repository.PetRepository
import javax.inject.Inject

class SynchronizePetsWithServerUseCase @Inject constructor(
    private val repository: PetRepository
) {
    suspend operator fun invoke() {
        repository.synchronizeWithServer()
    }
}