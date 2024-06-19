package edu.mirea.onebeattrue.mylittlepet.data.network.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerializedName("token") val token: String
)
