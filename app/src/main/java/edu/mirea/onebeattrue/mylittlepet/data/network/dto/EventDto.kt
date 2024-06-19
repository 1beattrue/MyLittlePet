package edu.mirea.onebeattrue.mylittlepet.data.network.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class EventDto(
    @SerializedName("id") val id: Int = UNDEFINED_ID,
    @SerializedName("time") val time: Long,
    @SerializedName("label") val label: String,
    @SerializedName("repeatable") val repeatable: Boolean,
    @SerializedName("petId") val petId: Int,
) {
    companion object {
        const val UNDEFINED_ID = -1
    }
}
