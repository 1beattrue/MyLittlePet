package edu.mirea.onebeattrue.mylittlepet.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import edu.mirea.onebeattrue.mylittlepet.data.local.model.EventDbModel
import edu.mirea.onebeattrue.mylittlepet.data.local.model.FullPetDbModel
import edu.mirea.onebeattrue.mylittlepet.data.local.model.MedicalDataDbModel
import edu.mirea.onebeattrue.mylittlepet.data.local.model.NoteDbModel
import edu.mirea.onebeattrue.mylittlepet.data.local.model.PetDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PetListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPet(petDbModel: PetDbModel)

    @Update
    suspend fun updatePet(petDbModel: PetDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEvent(eventDbModel: EventDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(noteDbModel: NoteDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMedicalData(medicalDataDbModel: MedicalDataDbModel)


    @Query("DELETE FROM pets WHERE id =:petId")
    suspend fun deletePet(petId: Int)

    @Query("DELETE FROM events WHERE petId = :petId AND id = :eventId")
    suspend fun deleteEvent(petId: Int, eventId: Int)

    @Query("DELETE FROM notes WHERE petId = :petId AND id = :noteId")
    suspend fun deleteNote(petId: Int, noteId: Int)

    @Query("DELETE FROM medical_data WHERE petId = :petId AND id = :medicalDataId")
    suspend fun deleteMedicalData(petId: Int, medicalDataId: Int)


    @Transaction
    @Query("SELECT * FROM pets")
    fun getPetList(): Flow<List<FullPetDbModel>>

    @Query("SELECT * FROM events WHERE petId = :petId")
    fun getEventList(petId: Int): Flow<List<EventDbModel>>

    @Query("SELECT * FROM notes WHERE petId = :petId")
    fun getNoteList(petId: Int): Flow<List<NoteDbModel>>

    @Query("SELECT * FROM medical_data WHERE petId = :petId")
    fun getMedicalDataList(petId: Int): Flow<List<MedicalDataDbModel>>

    @Transaction
    @Query("SELECT * FROM pets WHERE id = :petId")
    fun getPetById(petId: Int): Flow<FullPetDbModel>
}