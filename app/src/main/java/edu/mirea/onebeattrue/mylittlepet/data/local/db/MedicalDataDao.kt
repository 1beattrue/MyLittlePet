package edu.mirea.onebeattrue.mylittlepet.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import edu.mirea.onebeattrue.mylittlepet.data.local.model.MedicalDataDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicalDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMedicalData(medicalDataDbModel: MedicalDataDbModel)

    @Query("DELETE FROM medical_data WHERE petId = :petId AND id = :medicalDataId")
    suspend fun deleteMedicalData(petId: Int, medicalDataId: Int)

    @Query("SELECT * FROM medical_data WHERE petId = :petId")
    fun getMedicalDataList(petId: Int): Flow<List<MedicalDataDbModel>>
}