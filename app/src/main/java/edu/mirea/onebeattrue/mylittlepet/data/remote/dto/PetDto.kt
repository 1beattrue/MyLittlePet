package edu.mirea.onebeattrue.mylittlepet.data.remote.dto

import com.google.gson.annotations.SerializedName
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.PetType


data class PetDto(
    @SerializedName("id") val id: Int,

    @SerializedName("name") val name: String,
    @SerializedName("type") val type: PetType,
    @SerializedName("image") val image: ByteArray?,
    @SerializedName("dateOfBirth") val dateOfBirth: Long?,
    @SerializedName("weight") val weight: Float?,

    @SerializedName("eventList") val eventList: List<EventDto>,
    @SerializedName("noteList") val noteList: List<NoteDto>,
    @SerializedName("medicalDataList") val medicalDataList: List<MedicalDataDto>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PetDto

        if (id != other.id) return false
        if (name != other.name) return false
        if (type != other.type) return false
        if (!image.contentEquals(other.image)) return false
        if (dateOfBirth != other.dateOfBirth) return false
        if (weight != other.weight) return false
        if (eventList != other.eventList) return false
        if (noteList != other.noteList) return false
        if (medicalDataList != other.medicalDataList) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + image.contentHashCode()
        result = 31 * result + (dateOfBirth?.hashCode() ?: 0)
        result = 31 * result + (weight?.hashCode() ?: 0)
        result = 31 * result + eventList.hashCode()
        result = 31 * result + noteList.hashCode()
        result = 31 * result + medicalDataList.hashCode()
        return result
    }

}
