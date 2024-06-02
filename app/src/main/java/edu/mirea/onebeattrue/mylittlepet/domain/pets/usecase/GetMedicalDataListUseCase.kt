package edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase

import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalData
import edu.mirea.onebeattrue.mylittlepet.domain.pets.repository.MedicalDataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMedicalDataListUseCase @Inject constructor(private val repository: MedicalDataRepository) {
    operator fun invoke(petId: Int): Flow<List<MedicalData>> {
        return repository.getMedicalDataList(petId)
    }
}