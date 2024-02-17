package edu.mirea.onebeattrue.mylittlepet.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.PetType

@Composable
fun PetType.getName() = when(this) {
    PetType.CAT ->  stringResource(id = R.string.pet_type_cat)
    PetType.DOG ->  stringResource(id = R.string.pet_type_dog)
    PetType.RABBIT ->  stringResource(id = R.string.pet_type_rabbit)
    PetType.BIRD ->  stringResource(id = R.string.pet_type_bird)
    PetType.FISH ->  stringResource(id = R.string.pet_type_fish)
    PetType.SNAKE ->  stringResource(id = R.string.pet_type_snake)
    PetType.TIGER ->  stringResource(id = R.string.pet_type_tiger)
    PetType.MOUSE ->  stringResource(id = R.string.pet_type_mouse)
    PetType.TURTLE ->  stringResource(id = R.string.pet_type_turtle)
}