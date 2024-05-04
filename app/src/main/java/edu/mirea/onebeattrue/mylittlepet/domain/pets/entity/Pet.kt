package edu.mirea.onebeattrue.mylittlepet.domain.pets.entity

import android.net.Uri
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class Pet(
    val type: PetType,
    val name: String,
    @Contextual val imageUri: Uri,

    val dateOfBirth: Long? = null,
    val weight: Float? = null,

    val eventList: List<Event> = listOf(),
    val noteList: List<Note> = listOf(),
    val medicalDataList: List<MedicalData> = listOf(),

    val id: Int = UNDEFINED_ID
) {
    companion object {
        private const val UNDEFINED_ID = 0
    }
}

