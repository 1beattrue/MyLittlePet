package edu.mirea.onebeattrue.mylittlepet.domain.pets.repository

import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    suspend fun addEvent(petName: String, event: Event)
    suspend fun deleteEvent(petName: String, event: Event)

    fun getEventList(petId: Int): Flow<List<Event>>
    suspend fun deleteIrrelevantEvents(petName: String, petId: Int)
    suspend fun synchronizeWithServer(petName: String, petId: Int)
}