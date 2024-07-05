package edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase

import edu.mirea.onebeattrue.mylittlepet.domain.pets.repository.EventRepository
import javax.inject.Inject

class SynchronizeEventsWithServerUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(petId: Int) {
        repository.synchronizeWithServer(petId)
    }
}