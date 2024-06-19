package edu.mirea.onebeattrue.mylittlepet.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import edu.mirea.onebeattrue.mylittlepet.data.local.model.EventDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEvent(eventDbModel: EventDbModel)

    @Query("DELETE FROM events WHERE petId = :petId AND id = :eventId")
    suspend fun deleteEvent(petId: Int, eventId: Int)

    @Query("SELECT * FROM events WHERE petId = :petId")
    fun getEventList(petId: Int): Flow<List<EventDbModel>>
}