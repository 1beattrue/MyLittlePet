package edu.mirea.onebeattrue.mylittlepet.data.remote.dto

import com.google.gson.annotations.SerializedName


data class UserDto(
    @SerializedName("token") val token: String
)