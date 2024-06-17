package edu.mirea.onebeattrue.mylittlepet.data.repository

import edu.mirea.onebeattrue.mylittlepet.data.local.db.PetListDao
import edu.mirea.onebeattrue.mylittlepet.data.mapper.PetMapper
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalData
import edu.mirea.onebeattrue.mylittlepet.domain.pets.repository.MedicalDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MedicalDataRepositoryImpl @Inject constructor(
    private val petListDao: PetListDao,
    private val mapper: PetMapper
) : MedicalDataRepository {

    override suspend fun addMedicalData(medicalData: MedicalData) {
        petListDao.addMedicalData(mapper.mapMedicalDataEntityToDbModel(medicalData))
    }

    override suspend fun deleteMedicalData(medicalData: MedicalData) {
        petListDao.deleteMedicalData(
            petId = medicalData.petId,
            medicalDataId = medicalData.id
        )
    }

    override fun getMedicalDataList(petId: Int): Flow<List<MedicalData>> =
        petListDao.getMedicalDataList(petId).map { listDbModel ->
            listDbModel.map { mapper.mapMedicalDataDbModelToEntity(it) }
        }

}