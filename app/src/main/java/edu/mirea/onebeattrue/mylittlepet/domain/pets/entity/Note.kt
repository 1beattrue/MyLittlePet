package edu.mirea.onebeattrue.mylittlepet.domain.pets.entity

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val text: String,
    val iconResId: Int
)