package edu.mirea.onebeattrue.mylittlepet.data.repository

import edu.mirea.onebeattrue.mylittlepet.data.local.db.MedicalDataDao
import edu.mirea.onebeattrue.mylittlepet.data.mapper.ImageMapper
import edu.mirea.onebeattrue.mylittlepet.data.mapper.mapDbModelListToEntities
import edu.mirea.onebeattrue.mylittlepet.data.mapper.mapEntityToDbModel
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalData
import edu.mirea.onebeattrue.mylittlepet.domain.pets.repository.MedicalDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MedicalDataRepositoryImpl @Inject constructor(
    private val petListDao: MedicalDataDao,
    private val imageMapper: ImageMapper
) : MedicalDataRepository {

    override suspend fun addMedicalData(medicalData: MedicalData) {
        petListDao.addMedicalData(medicalData.mapEntityToDbModel(imageMapper))
    }

    override suspend fun deleteMedicalData(medicalData: MedicalData) {
        petListDao.deleteMedicalData(
            petId = medicalData.petId,
            medicalDataId = medicalData.id
        )
    }

    override fun getMedicalDataList(petId: Int): Flow<List<MedicalData>> =
        petListDao.getMedicalDataList(petId).map {
            it.mapDbModelListToEntities(imageMapper)
        }

}