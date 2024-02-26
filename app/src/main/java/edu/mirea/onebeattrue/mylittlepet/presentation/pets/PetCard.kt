package edu.mirea.onebeattrue.mylittlepet.presentation.pets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.PetType
import edu.mirea.onebeattrue.mylittlepet.extensions.getImageId
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCardDefaultElevation
import edu.mirea.onebeattrue.mylittlepet.ui.theme.CORNER_RADIUS_CONTAINER
import edu.mirea.onebeattrue.mylittlepet.ui.theme.MENU_ITEM_PADDING
import edu.mirea.onebeattrue.mylittlepet.ui.theme.MyLittlePetTheme

@Composable
fun PetCard(
    modifier: Modifier = Modifier,
    pet: Pet,
    deletePet: () -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = modifier
    ) {
        CustomCardDefaultElevation(
            modifier = Modifier
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
                    id = pet.type.getImageId()
                ),
                contentDescription = null,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .wrapContentSize(Alignment.TopEnd)
        ) {
            IconButton(
                onClick = { expanded = !expanded },
            ) {
                Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = null)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    modifier = Modifier.clip(RoundedCornerShape(CORNER_RADIUS_CONTAINER)),
                    contentPadding = MENU_ITEM_PADDING,
                    text = {
                        Text(
                            text = stringResource(id = R.string.edit),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = null
                        )
                    },
                    onClick = { /* TODO(): */ }
                )
                DropdownMenuItem(
                    modifier = Modifier.clip(RoundedCornerShape(CORNER_RADIUS_CONTAINER)),
                    contentPadding = MENU_ITEM_PADDING,
                    text = {
                        Text(
                            text = stringResource(id = R.string.delete),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = null
                        )
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = Color.Red,
                        trailingIconColor = Color.Red
                    ),
                    onClick = { deletePet() }
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