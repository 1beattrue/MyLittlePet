package edu.mirea.onebeattrue.mylittlepet.data.pets

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PetListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPet(petDbModel: PetDbModel)

    @Query("DELETE FROM pets WHERE id =:petId")
    suspend fun deletePet(petId: Int)

    @Query("SELECT * FROM pets")
    fun getPetList(): Flow<List<PetDbModel>>
}