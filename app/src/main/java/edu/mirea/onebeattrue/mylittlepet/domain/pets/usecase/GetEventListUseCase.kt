package edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase

import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Event
import edu.mirea.onebeattrue.mylittlepet.domain.pets.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEventListUseCase @Inject constructor(private val repository: EventRepository) {
    operator fun invoke(petId: Int): Flow<List<Event>> {
        return repository.getEventList(petId)
    }
}