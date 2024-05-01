package edu.mirea.onebeattrue.mylittlepet.domain.pets.entity

import androidx.compose.runtime.Composable
import edu.mirea.onebeattrue.mylittlepet.extensions.getName

enum class PetType {
    NOT_SELECTED, CAT, DOG, RABBIT, BIRD, FISH, SNAKE, TIGER, MOUSE, TURTLE;

    companion object {
        @Composable
        fun getNames(): List<String> = entries.map { it.getName() }

        fun getTypes(): List<PetType> = entries
    }
}