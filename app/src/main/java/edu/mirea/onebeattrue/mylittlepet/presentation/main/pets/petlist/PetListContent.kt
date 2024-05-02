package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.petlist

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.extensions.getImageId
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCardExtremeElevation
import edu.mirea.onebeattrue.mylittlepet.ui.theme.CORNER_RADIUS_CONTAINER
import edu.mirea.onebeattrue.mylittlepet.ui.theme.MENU_ITEM_PADDING

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PetListContent(
    modifier: Modifier = Modifier,
    component: PetListComponent
) {
    val state by component.model.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                title = {
                    Text(
                        style = MaterialTheme.typography.titleLarge,
                        text = stringResource(R.string.pets_app_bar_title)
                    )
                },
                actions = {
                    IconButton(
                        onClick = { component.addPet() }
                    ) {
                        Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
                    }
                }
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(
                vertical = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items = state.petList, key = { it.id }) { pet ->
                PetCard(
                    modifier = Modifier.animateItemPlacement(),
                    pet = pet,
                    deletePet = { component.deletePet(pet) }
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun PetCard(
    modifier: Modifier = Modifier,
    pet: Pet,
    deletePet: () -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = modifier
    ) {
        CustomCardExtremeElevation {
            Text(
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                text = pet.name,
            )
            Box(
                modifier = Modifier.clip(RoundedCornerShape(CORNER_RADIUS_CONTAINER))
            ) {
                if (pet.imageUri == Uri.EMPTY) {
                    Image(
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop,
                        painter = painterResource(id = pet.type.getImageId()),
                        contentDescription = null
                    )
                } else {
                    GlideImage(
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Fit,
                        model = pet.imageUri,
                        contentDescription = null
                    )
                }
            }
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
            MaterialTheme(
                shapes = MaterialTheme.shapes.copy(
                    extraSmall = RoundedCornerShape(
                        CORNER_RADIUS_CONTAINER
                    )
                )
            ) {
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
}