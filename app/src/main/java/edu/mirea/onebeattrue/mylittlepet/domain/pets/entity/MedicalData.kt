package edu.mirea.onebeattrue.mylittlepet.domain.pets.entity

import android.net.Uri
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class MedicalData(
    val name: String,
    val date: String,
    val time: String,
    @Contextual
    val imageUri: Uri,
    val note: String
)