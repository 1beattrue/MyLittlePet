package edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase

import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Event
import edu.mirea.onebeattrue.mylittlepet.domain.pets.repository.EventRepository
import javax.inject.Inject

class AddEventUseCase @Inject constructor(private val repository: EventRepository) {
    suspend operator fun invoke(petName: String, event: Event) {
        repository.addEvent(petName, event)
    }
}