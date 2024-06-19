package edu.mirea.onebeattrue.mylittlepet.data.network.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class MedicalDataDto(
    @SerializedName("id") val id: Int = UNDEFINED_ID,
    @SerializedName("type") val type: String,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("text") val text: String,
    @SerializedName("petId") val petId: Int
) {
    companion object {
        const val UNDEFINED_ID = -1
    }
}
