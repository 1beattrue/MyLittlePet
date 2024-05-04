package edu.mirea.onebeattrue.mylittlepet.domain.pets.entity

import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val date: String,
    val label: String
)