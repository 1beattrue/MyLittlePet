package edu.mirea.onebeattrue.mylittlepet.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ErrorDto(
    @SerializedName("message") val message: String
)
