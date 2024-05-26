package edu.mirea.onebeattrue.mylittlepet.domain.pets.entity

import androidx.compose.runtime.Composable
import edu.mirea.onebeattrue.mylittlepet.extensions.getName

enum class MedicalDataType {
    VACCINATION, ANALYSIS, ALLERGY, TREATMENT, OTHER;

    companion object {
        @Composable
        fun getNames(): List<String> = MedicalDataType.entries.map { it.getName() }

        fun getTypes(): List<MedicalDataType> = MedicalDataType.entries
    }
}
