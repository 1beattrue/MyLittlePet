package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.medicaldatalist

import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalData
import kotlinx.coroutines.flow.StateFlow

interface MedicalDataListComponent {
    val model: StateFlow<MedicalDataListStore.State>

    fun onAddMedicalData()

    fun onDeleteMedicalData(medicalData: MedicalData)

    fun onBackClicked()
}