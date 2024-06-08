package edu.mirea.onebeattrue.mylittlepet.domain.auth.entity

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long,
    val token: String
)
