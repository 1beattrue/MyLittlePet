package edu.mirea.onebeattrue.mylittlepet.data.network.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class PetDto(
    @SerializedName("id") val id: Int = UNDEFINED_ID,
    @SerializedName("type") val type: String,
    @SerializedName("name") val name: String,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("dateOfBirth") val dateOfBirth: Long?,
    @SerializedName("weight") val weight: Float?,
    @SerializedName("userToken") val userToken: String
) {
    companion object {
        const val UNDEFINED_ID = -1
    }
}
