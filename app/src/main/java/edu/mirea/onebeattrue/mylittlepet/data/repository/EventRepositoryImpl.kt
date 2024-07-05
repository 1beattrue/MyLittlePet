package edu.mirea.onebeattrue.mylittlepet.data.repository

import edu.mirea.onebeattrue.mylittlepet.data.local.db.EventDao
import edu.mirea.onebeattrue.mylittlepet.data.mapper.mapDbModelListToEntities
import edu.mirea.onebeattrue.mylittlepet.data.mapper.mapEntityToDbModel
import edu.mirea.onebeattrue.mylittlepet.data.mapper.mapEntityToDto
import edu.mirea.onebeattrue.mylittlepet.data.network.api.EventApiService
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.AlarmItem
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.AlarmScheduler
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Event
import edu.mirea.onebeattrue.mylittlepet.domain.pets.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val eventDao: EventDao,
    private val alarmScheduler: AlarmScheduler,
    private val eventApiService: EventApiService
) : EventRepository {

    override suspend fun addEvent(event: Event) {
        val eventId = eventApiService.createEvent(event.mapEntityToDto())

        eventDao.addEvent(event.mapEntityToDbModel().copy(id = eventId))
    }

    override suspend fun deleteEvent(petName: String, event: Event) {

        eventApiService.deleteEvent(event.id)

        alarmScheduler.cancel(
            AlarmItem(
                time = event.time,
                title = petName,
                text = event.label,
                repeatable = event.repeatable
            )
        )

        eventDao.deleteEvent(
            petId = event.petId,
            eventId = event.id
        )
    }

    override fun getEventList(petId: Int): Flow<List<Event>> =
        eventDao.getEventList(petId).map { listDbModel ->
            listDbModel.mapDbModelListToEntities()
        }

}