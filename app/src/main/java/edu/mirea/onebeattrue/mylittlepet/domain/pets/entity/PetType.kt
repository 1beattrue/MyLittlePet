package edu.mirea.onebeattrue.mylittlepet.domain.pets.entity

import androidx.compose.runtime.Composable
import edu.mirea.onebeattrue.mylittlepet.extensions.getName

enum class PetType {
    CAT, DOG, RABBIT, BIRD, FISH, SNAKE, TIGER, MOUSE, TURTLE;

    companion object  {
        @Composable
        fun getNames(): List<String> = PetType.values().toList().map { it.getName() }

        fun getTypes(): List<PetType> = PetType.values().toList()
    }
}