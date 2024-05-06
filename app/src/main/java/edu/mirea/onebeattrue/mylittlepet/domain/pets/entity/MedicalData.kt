package edu.mirea.onebeattrue.mylittlepet.domain.pets.entity

import android.net.Uri
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class MedicalData(
    val name: String,
    val date: Long,
    val time: Long,
    @Contextual
    val imageUri: Uri,
    val note: String
)