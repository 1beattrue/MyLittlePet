package edu.mirea.onebeattrue.mylittlepet.domain.pets.entity

data class Pet(
    val type: PetType,
    val name: String,
    val picture: String,
    val id: Int = UNDEFINED_ID
) {
    companion object {
        private const val UNDEFINED_ID = 0
    }
}