package edu.mirea.onebeattrue.mylittlepet.domain.pets.entity

import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val date: Long?,
    val hours: Int,
    val minutes: Int,
    val label: String,
    val id: Int
)