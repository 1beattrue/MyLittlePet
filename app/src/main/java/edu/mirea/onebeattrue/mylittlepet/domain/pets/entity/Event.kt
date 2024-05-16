package edu.mirea.onebeattrue.mylittlepet.domain.pets.entity

import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val id: Int,
    val time: Long,
    val label: String,
    val repeatable: Boolean
)