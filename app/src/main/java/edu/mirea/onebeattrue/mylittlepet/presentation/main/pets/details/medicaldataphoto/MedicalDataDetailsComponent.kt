package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.medicaldataphoto

import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalData

interface MedicalDataDetailsComponent {
    val medicalData: MedicalData

    fun downloadPhoto()
    fun onBackClick()
}