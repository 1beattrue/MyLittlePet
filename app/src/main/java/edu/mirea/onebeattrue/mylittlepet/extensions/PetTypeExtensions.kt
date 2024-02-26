package edu.mirea.onebeattrue.mylittlepet.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.PetType

fun PetType.getStringId(): Int = when (this) {
    PetType.CAT -> R.string.pet_type_cat
    PetType.DOG -> R.string.pet_type_dog
    PetType.RABBIT -> R.string.pet_type_rabbit
    PetType.BIRD -> R.string.pet_type_bird
    PetType.FISH -> R.string.pet_type_fish
    PetType.SNAKE -> R.string.pet_type_snake
    PetType.TIGER -> R.string.pet_type_tiger
    PetType.MOUSE -> R.string.pet_type_mouse
    PetType.TURTLE -> R.string.pet_type_turtle
}

fun PetType.getImageId(): Int = when (this) {
    PetType.CAT -> R.drawable.image_cat_black
    PetType.DOG -> R.drawable.image_dog_brown
    PetType.RABBIT -> R.drawable.image_rabbit
    PetType.BIRD -> R.drawable.image_bird
    PetType.FISH -> R.drawable.image_fish_blue
    PetType.SNAKE -> R.drawable.image_snake
    PetType.TIGER -> R.drawable.image_tiger
    PetType.MOUSE -> R.drawable.image_mouse
    PetType.TURTLE -> R.drawable.image_turtle
}

@Composable
fun PetType.getName(): String = stringResource(id = this.getStringId())