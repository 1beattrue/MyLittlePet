package edu.mirea.onebeattrue.mylittlepet.data.remote.dto

import com.google.gson.annotations.SerializedName

data class EventDto (
    @SerializedName("id") val id: Int,

    @SerializedName("time") val time: Long,
    @SerializedName("label") val label: String,
    @SerializedName("repeatable") val repeatable: Boolean
)