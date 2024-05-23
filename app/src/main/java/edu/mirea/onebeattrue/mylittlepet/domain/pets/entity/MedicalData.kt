package edu.mirea.onebeattrue.mylittlepet.domain.pets.entity

import android.net.Uri
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class MedicalData(
    val id: Int,
    val type: MedicalDataType,
    @Contextual val imageUri: Uri,
    val text: String
)