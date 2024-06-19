package edu.mirea.onebeattrue.mylittlepet.data.network.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class NoteDto(
    @SerializedName("id") val id: Int = UNDEFINED_ID,
    @SerializedName("text") val text: String,
    @SerializedName("iconResId") val iconResId: Int,
    @SerializedName("petId") val petId: Int,
) {
    companion object {
        const val UNDEFINED_ID = -1
    }
}
