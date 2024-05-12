package edu.mirea.onebeattrue.mylittlepet.domain.pets.entity

import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val time: Long,
    val label: String,
    val id: Int,
    val repeatable: Boolean
)