package edu.mirea.onebeattrue.mylittlepet.domain.pets.repository

import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalData
import kotlinx.coroutines.flow.Flow

interface MedicalDataRepository {
    suspend fun addMedicalData(medicalData: MedicalData)
    suspend fun deleteMedicalData(medicalData: MedicalData)

    fun getMedicalDataList(petId: Int): Flow<List<MedicalData>>
}