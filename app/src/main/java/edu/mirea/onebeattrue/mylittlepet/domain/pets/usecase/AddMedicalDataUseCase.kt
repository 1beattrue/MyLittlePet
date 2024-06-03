package edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase

import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalData
import edu.mirea.onebeattrue.mylittlepet.domain.pets.repository.MedicalDataRepository
import javax.inject.Inject

class AddMedicalDataUseCase @Inject constructor(private val repository: MedicalDataRepository) {
    suspend operator fun invoke(medicalData: MedicalData) {
        repository.addMedicalData(medicalData)
    }
}