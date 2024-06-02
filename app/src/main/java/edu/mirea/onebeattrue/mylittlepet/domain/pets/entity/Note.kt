package edu.mirea.onebeattrue.mylittlepet.domain.pets.entity

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val text: String,
    val iconResId: Int,

    val id: Int = UNDEFINED_ID,
    val petId: Int
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}