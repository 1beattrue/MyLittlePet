package edu.mirea.onebeattrue.mylittlepet.data.remote.dto

import com.google.gson.annotations.SerializedName
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalDataType

data class MedicalDataDto(
    @SerializedName("id") val id: Int,

    @SerializedName("type") val type: MedicalDataType,
    @SerializedName("image") val image: ByteArray,
    @SerializedName("text") val text: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MedicalDataDto

        if (id != other.id) return false
        if (type != other.type) return false
        if (!image.contentEquals(other.image)) return false
        if (text != other.text) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + type.hashCode()
        result = 31 * result + image.contentHashCode()
        result = 31 * result + text.hashCode()
        return result
    }
}