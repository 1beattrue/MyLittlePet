package edu.mirea.onebeattrue.mylittlepet.domain.pets.entity

import kotlinx.serialization.Serializable

@Serializable
data class MedicalData(
    val type: MedicalDataType,
    val imageUri: String,
    val text: String,

    val id: Int = UNDEFINED_ID,
    val petId: Int
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}