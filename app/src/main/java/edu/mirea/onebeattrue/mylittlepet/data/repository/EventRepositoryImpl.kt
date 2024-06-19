package edu.mirea.onebeattrue.mylittlepet.data.repository

import edu.mirea.onebeattrue.mylittlepet.data.local.db.EventDao
import edu.mirea.onebeattrue.mylittlepet.data.mapper.mapDbModelListToEntities
import edu.mirea.onebeattrue.mylittlepet.data.mapper.mapEntityToDbModel
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.AlarmItem
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.AlarmScheduler
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Event
import edu.mirea.onebeattrue.mylittlepet.domain.pets.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val petListDao: EventDao,
    private val alarmScheduler: AlarmScheduler
) : EventRepository {
    override suspend fun addEvent(event: Event) {
        petListDao.addEvent(event.mapEntityToDbModel())
    }

    override suspend fun deleteEvent(petName: String, event: Event) {
        alarmScheduler.cancel(
            AlarmItem(
                time = event.time,
                title = petName,
                text = event.label,
                repeatable = event.repeatable
            )
        )

        petListDao.deleteEvent(
            petId = event.petId,
            eventId = event.id
        )
    }

    override fun getEventList(petId: Int): Flow<List<Event>> =
        petListDao.getEventList(petId).map { listDbModel ->
            listDbModel.mapDbModelListToEntities()
        }

}