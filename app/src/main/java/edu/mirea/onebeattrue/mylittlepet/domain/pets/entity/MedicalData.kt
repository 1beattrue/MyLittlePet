package edu.mirea.onebeattrue.mylittlepet.domain.pets.entity

import kotlinx.serialization.Serializable

@Serializable
data class MedicalData(
    val id: Int,
    val type: MedicalDataType,
    val imageUri: String,
    val text: String
)