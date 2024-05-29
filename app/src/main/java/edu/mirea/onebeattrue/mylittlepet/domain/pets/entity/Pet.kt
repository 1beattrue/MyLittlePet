package edu.mirea.onebeattrue.mylittlepet.domain.pets.entity

import kotlinx.serialization.Serializable
import java.util.Calendar

@Serializable
data class Pet(
    val type: PetType,
    val name: String,
    val imageUri: String,

    val dateOfBirth: Long? = null,
    val weight: Float? = null,

    val eventList: List<Event> = listOf(),
    val noteList: List<Note> = listOf(),
    val medicalDataList: List<MedicalData> = listOf(),

    val id: Int = UNDEFINED_ID
) {
    fun isUnread(): Boolean {
        val currentTime = Calendar.getInstance().timeInMillis
        return eventList.any { it.time <= currentTime && !it.repeatable }
    }

    companion object {
        private const val UNDEFINED_ID = 0
    }
}

