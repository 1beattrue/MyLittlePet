package edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase

import edu.mirea.onebeattrue.mylittlepet.domain.pets.repository.EventRepository
import javax.inject.Inject

class DeleteIrrelevantEventsUseCase @Inject constructor(private val repository: EventRepository) {
    suspend operator fun invoke(petName: String, petId: Int) {
        repository.deleteIrrelevantEvents(petName, petId)
    }
}