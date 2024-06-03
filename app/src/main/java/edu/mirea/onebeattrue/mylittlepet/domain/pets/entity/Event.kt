package edu.mirea.onebeattrue.mylittlepet.domain.pets.entity

import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val time: Long,
    val label: String,
    val repeatable: Boolean,

    val id: Int = UNDEFINED_ID,
    val petId: Int
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}