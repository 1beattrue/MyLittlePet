package edu.mirea.onebeattrue.mylittlepet.domain.pets.entity

import android.net.Uri

data class Pet(
    val type: PetType,
    val name: String,
    val imageUri: Uri,
    val id: Int = UNDEFINED_ID
) {
    companion object {
        private const val UNDEFINED_ID = 0
    }
}