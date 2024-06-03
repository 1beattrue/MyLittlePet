package edu.mirea.onebeattrue.mylittlepet.domain.pets.repository

import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    suspend fun addEvent(event: Event)
    suspend fun deleteEvent(petName: String, event: Event)

    fun getEventList(petId: Int): Flow<List<Event>>
}