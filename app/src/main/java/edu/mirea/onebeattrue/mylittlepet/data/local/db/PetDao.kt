package edu.mirea.onebeattrue.mylittlepet.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import edu.mirea.onebeattrue.mylittlepet.data.local.model.PetDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPet(petDbModel: PetDbModel)

    @Query("DELETE FROM pets WHERE id =:petId")
    suspend fun deletePet(petId: Int)

    @Query("SELECT * FROM pets")
    fun getPetList(): Flow<List<PetDbModel>>

    @Query("SELECT * FROM pets WHERE id = :petId")
    fun getPetById(petId: Int): Flow<PetDbModel>
}