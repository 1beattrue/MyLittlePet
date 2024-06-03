package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.petlist

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.PetType
import edu.mirea.onebeattrue.mylittlepet.extensions.getImageId
import edu.mirea.onebeattrue.mylittlepet.ui.customview.ClickableCustomCard
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCardWithAddButton
import edu.mirea.onebeattrue.mylittlepet.ui.theme.CORNER_RADIUS_CONTAINER
import edu.mirea.onebeattrue.mylittlepet.ui.theme.EXTREME_ELEVATION
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
            when (val screenState = state.screenState) {
                PetListStore.State.ScreenState.Empty -> {
                    item {
                        AddFirstPetCard {
                            component.addPet()
                        }
                    }
                }

                is PetListStore.State.ScreenState.Loaded -> {
                    items(items = screenState.petList, key = { it.id }) { pet ->
                        PetCard(
                            modifier = Modifier
                                .animateItemPlacement(),
                            pet = pet,
                            deletePet = { component.deletePet(pet) },
                            editPet = {
                                component.editPet(pet)
                            },
                            openDetails = {
                                component.openDetails(pet)
                            }
                        )
                    }
                }

                PetListStore.State.ScreenState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AddFirstPetCard(
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit
) {
    CustomCardWithAddButton(
        modifier = modifier,
        onAddClick = { onAddClick() }
    ) {
        Text(
            style = MaterialTheme.typography.titleLarge,
            text = stringResource(R.string.add_your_first_pet)
        )
        val petImageId by remember { mutableIntStateOf(PetType.getTypes().random().getImageId()) }
        Image(
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop,
            painter = painterResource(petImageId),
            contentDescription = null
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun PetCard(
    modifier: Modifier = Modifier,
    pet: Pet,
    deletePet: () -> Unit,
    editPet: () -> Unit,
    openDetails: () -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }

    ClickableCustomCard(
        modifier = modifier,
        elevation = EXTREME_ELEVATION,
        onClick = { openDetails() },
        innerPadding = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            if (pet.isUnread()) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(
                            top = 16.dp,
                            start = 24.dp
                        )
                        .rotate(-45f),
                    painter = painterResource(R.drawable.ic_pets),
                    contentDescription = null,
                    tint = Color.Red
                )
            }

            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(
                        top = 32.dp,
                        start = 64.dp,
                        end = 64.dp
                    ),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                text = pet.name,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(
                        top = 16.dp,
                        end = 8.dp
                    )
            ) {
                IconButton(
                    modifier = Modifier
                        .align(Alignment.CenterStart),
                    onClick = { expanded = !expanded }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = null,
                    )
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
                            onClick = {
                                expanded = false
                                editPet()
                            }
                        )

                        DropdownMenuItem(
                            contentPadding = MENU_ITEM_PADDING,
                            text = {
                                Text(
                                    text = stringResource(id = R.string.details),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Info,
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                expanded = false
                                openDetails()
                            }
                        )

                        HorizontalDivider()

                        DropdownMenuItem(
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
                            onClick = {
                                expanded = false
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .padding(
                    start = 32.dp,
                    end = 32.dp,
                    bottom = 32.dp
                )
                .clip(RoundedCornerShape(CORNER_RADIUS_CONTAINER))
        ) {
            if (Uri.parse(pet.imageUri) == Uri.EMPTY) {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(id = pet.type.getImageId()),
                    contentDescription = null
                )
            } else {
                GlideImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    model = pet.imageUri,
                    contentDescription = null
                )
            }
        }
    }

    if (showDeleteDialog) {
        DeletePetDialog(
            onDismissRequest = { showDeleteDialog = false },
            onConfirmation = {
                showDeleteDialog = false
                deletePet()
            }
        )
    }
}

@Composable
private fun DeletePetDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = { onDismissRequest() },
        icon = {
            Icon(
                imageVector = Icons.Rounded.Warning,
                contentDescription = null,
                tint = Color.Red
            )
        },
        title = {
            Text(text = stringResource(R.string.delete_dialog_title))
        },
        text = {
            Text(
                text = stringResource(R.string.delete_dialog_text),
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(
                    text = stringResource(R.string.delete),
                    color = Color.Red
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}