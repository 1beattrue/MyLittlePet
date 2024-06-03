package edu.mirea.onebeattrue.mylittlepet.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NoteDto (
    @SerializedName("id") val id: Int,

    @SerializedName("text") val text: String,
    @SerializedName("iconResId") val iconResId: Int
)
