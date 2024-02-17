package edu.mirea.onebeattrue.mylittlepet.presentation.pets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.PetType
import edu.mirea.onebeattrue.mylittlepet.ui.theme.MyLittlePetTheme
import edu.mirea.onebeattrue.mylittlepet.ui.theme.ROUNDED_CORNER_SIZE_SURFACE
import edu.mirea.onebeattrue.mylittlepet.ui.theme.SMALL_ELEVATION

@Composable
fun PetCard(
    modifier: Modifier = Modifier,
    pet: Pet,
    deletePet: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(ROUNDED_CORNER_SIZE_SURFACE),
        elevation = CardDefaults.cardElevation(defaultElevation = SMALL_ELEVATION)
    ) {
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = { deletePet() },
                ) {
                    Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = null)
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    text = pet.name,
                )
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(
                        id = when (pet.type) {
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
                    ),
                    contentDescription = null,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PetCardPreviewLight() {
    MyLittlePetTheme(darkTheme = false) {
        PetCard(pet = Pet(PetType.CAT, "Cat", "")) {}
    }
}

@Preview
@Composable
private fun PetCardPreviewDark() {
    MyLittlePetTheme(darkTheme = true) {
        PetCard(pet = Pet(PetType.DOG, "Dog", "")) {}
    }
}