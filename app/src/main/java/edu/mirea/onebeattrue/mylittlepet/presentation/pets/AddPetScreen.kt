package edu.mirea.onebeattrue.mylittlepet.presentation.pets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.PetType
import edu.mirea.onebeattrue.mylittlepet.extensions.getImageId
import edu.mirea.onebeattrue.mylittlepet.extensions.getName
import edu.mirea.onebeattrue.mylittlepet.presentation.ViewModelFactory
import edu.mirea.onebeattrue.mylittlepet.ui.theme.ROUNDED_CORNER_SIZE_CONTAINER
import edu.mirea.onebeattrue.mylittlepet.ui.theme.ROUNDED_CORNER_SIZE_SURFACE
import edu.mirea.onebeattrue.mylittlepet.ui.theme.SMALL_ELEVATION

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetScreen(
    modifier: Modifier = Modifier,
    viewModelFactory: ViewModelFactory,
    close: () -> Unit
) {
    val viewModel: AddPetViewModel = viewModel(factory = viewModelFactory)
    val petTypes = PetType.getTypes()

    val initialSelect = stringResource(id = R.string.initial_pet_type)
    var selectedTypeName by rememberSaveable { mutableStateOf(initialSelect) }
    var selectedType by rememberSaveable { mutableStateOf<PetType?>(null) }

    var expanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(ROUNDED_CORNER_SIZE_SURFACE),
            elevation = CardDefaults.cardElevation(defaultElevation = SMALL_ELEVATION)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 32.dp,
                        vertical = 32.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(
                    visible = true,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Text(
                        text = stringResource(id = R.string.select_pet_type),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Box {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = {
                            expanded = !expanded
                        }
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            value = selectedTypeName,
                            shape = RoundedCornerShape(ROUNDED_CORNER_SIZE_CONTAINER),
                            onValueChange = {
                                // TODO(): выбранный элемент обработать тут
                            },
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            petTypes.forEach { petType ->
                                val petTypeName = petType.getName()
                                DropdownMenuItem(
                                    modifier = Modifier
                                        .clip(
                                            RoundedCornerShape(ROUNDED_CORNER_SIZE_CONTAINER)
                                        ),
                                    contentPadding = PaddingValues(
                                        start = 16.dp,
                                        end = 16.dp,
                                        top = 8.dp,
                                        bottom = 8.dp
                                    ),
                                    text = {
                                        Text(
                                            text = petTypeName,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    },
                                    trailingIcon = {
                                        Icon(
                                            painter = painterResource(id = petType.getImageId()),
                                            contentDescription = null
                                        )
                                    },
                                    onClick = {
                                        selectedType = petType
                                        selectedTypeName = petTypeName
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            viewModel.addPet(
                                Pet(
                                    type = selectedType!!,
                                    name = "Pet Name",
                                    picture = ""
                                )
                            )
                            close()
                        },
                        shape = RoundedCornerShape(ROUNDED_CORNER_SIZE_CONTAINER)
                    ) {
                        Text(
                            text = stringResource(
                                id = R.string.next
                            ),
                            fontSize = 16.sp
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}