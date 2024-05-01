package edu.mirea.onebeattrue.mylittlepet.presentation.pets

import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.PetType
import edu.mirea.onebeattrue.mylittlepet.extensions.getName
import edu.mirea.onebeattrue.mylittlepet.ui.animation.ANIMATION_TRANSITION_FADE_IN_FADE_OUT
import edu.mirea.onebeattrue.mylittlepet.ui.animation.ANIMATION_TRANSITION_SLIDE_FROM_END_TO_START
import edu.mirea.onebeattrue.mylittlepet.ui.animation.ANIMATION_TRANSITION_SLIDE_FROM_START_TO_END
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomAddButton
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomBackButton
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCardDefaultElevation
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomNextButton
import edu.mirea.onebeattrue.mylittlepet.ui.theme.CORNER_RADIUS_CONTAINER
import edu.mirea.onebeattrue.mylittlepet.ui.theme.MENU_ITEM_PADDING

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetScreen(
    modifier: Modifier = Modifier,
    closeScreen: () -> Unit
) {
    // states --------------------------------------------------------------------------------------
    val petType = rememberSaveable {
        mutableStateOf<PetType?>(null)
    }
    val petName = rememberSaveable {
        mutableStateOf("")
    }
    val petImage = rememberSaveable {
        mutableStateOf("")
    }

    val initialSelect = stringResource(id = R.string.initial_pet_type)
    val selectedTypeName = rememberSaveable {
        mutableStateOf(initialSelect)
    }
    val expanded = rememberSaveable {
        mutableStateOf(false)
    }

    val isTypeTextFieldError = rememberSaveable {
        mutableStateOf(false)
    }

    val isNameTextFieldError = rememberSaveable {
        mutableStateOf(false)
    }

    val viewModel: AddPetViewModel = viewModel()

    val petTypes = PetType.getTypes()

    val addPetScreenState by viewModel.screenState.collectAsState(
        AddPetScreenState.Initial
    )
    // ---------------------------------------------------------------------------------------------

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
                        text = stringResource(R.string.add_pet_app_bar_title)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { closeScreen() },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedContent(
                label = "add_pet_anim",
                targetState = addPetScreenState,
                transitionSpec = {
                    when {
                        initialState is AddPetScreenState.SelectPetType
                                && targetState is AddPetScreenState.SelectPetName -> {
                            ANIMATION_TRANSITION_SLIDE_FROM_END_TO_START
                        }

                        initialState is AddPetScreenState.SelectPetName
                                && targetState is AddPetScreenState.SelectPetImage -> {
                            ANIMATION_TRANSITION_SLIDE_FROM_END_TO_START
                        }

                        initialState is AddPetScreenState.SelectPetImage
                                && targetState is AddPetScreenState.SelectPetName -> {
                            ANIMATION_TRANSITION_SLIDE_FROM_START_TO_END
                        }

                        initialState is AddPetScreenState.SelectPetName
                                && targetState is AddPetScreenState.SelectPetType -> {
                            ANIMATION_TRANSITION_SLIDE_FROM_START_TO_END
                        }

                        else -> ANIMATION_TRANSITION_FADE_IN_FADE_OUT
                    }
                }
            ) { screenState ->
                when (screenState) {
                    is AddPetScreenState.SelectPetType -> {
                        isTypeTextFieldError.value = screenState.isInvalidType

                        CustomCardDefaultElevation {
                            Text(
                                text = stringResource(id = R.string.select_pet_type),
                                style = MaterialTheme.typography.titleLarge
                            )
                            SelectPetType(
                                expanded = expanded,
                                selectedTypeName = selectedTypeName,
                                isError = isTypeTextFieldError,
                                petTypes = petTypes,
                                selectedType = petType
                            )
                            CustomNextButton(onClick = { viewModel.moveNextToEnterName(petType.value) })
                        }
                    }

                    is AddPetScreenState.SelectPetName -> {
                        isNameTextFieldError.value = screenState.isInvalidName

                        CustomCardDefaultElevation {
                            Text(
                                text = stringResource(id = R.string.enter_pet_name),
                                style = MaterialTheme.typography.titleLarge
                            )
                            EnterPetNameTextField(
                                petName = petName,
                                isError = isNameTextFieldError
                            )
                            Box {
                                CustomNextButton(onClick = { viewModel.moveNextToSelectImage(petName.value) })
                                CustomBackButton(onClick = { viewModel.moveBackToSelectType() })
                            }
                        }
                    }

                    AddPetScreenState.SelectPetImage -> {
                        CustomCardDefaultElevation {
                            Text(
                                text = stringResource(id = R.string.add_pet_photo),
                                style = MaterialTheme.typography.titleLarge
                            )
                            Box {
                                CustomAddButton(
                                    onClick = {
                                        viewModel.addPet(
                                            Pet(
                                                type = petType.value
                                                    ?: throw RuntimeException("petType = null"),
                                                name = petName.value,
                                                imageUri = Uri.EMPTY,
                                            )
                                        )
                                    }
                                )
                                CustomBackButton(onClick = { viewModel.moveBackToSelectName() })
                            }
                        }
                    }

                    AddPetScreenState.Success -> {
                        closeScreen()
                    }

                    AddPetScreenState.Initial -> {}
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectPetType(
    modifier: Modifier = Modifier,
    expanded: MutableState<Boolean>,
    selectedTypeName: MutableState<String>,
    isError: MutableState<Boolean>,
    selectedType: MutableState<PetType?>,
    petTypes: List<PetType>,
) {
    Box(
        modifier = modifier
    ) {
        MaterialTheme(
            shapes = MaterialTheme.shapes.copy(
                extraSmall = RoundedCornerShape(
                    CORNER_RADIUS_CONTAINER
                )
            )
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded.value,
                onExpandedChange = {
                    expanded.value = !expanded.value
                }
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    value = selectedTypeName.value,
                    shape = RoundedCornerShape(CORNER_RADIUS_CONTAINER),
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        if (isError.value) {
                            Icon(imageVector = Icons.Rounded.Warning, contentDescription = null)
                        }
                    },
                    isError = isError.value,
                    supportingText = {
                        if (isError.value) {
                            Text(text = stringResource(id = R.string.error_pet_type))
                        }
                    },
                    leadingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
                    }
                )
                ExposedDropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false }
                ) {
                    petTypes.forEach { petType ->
                        val petTypeName = petType.getName()
                        DropdownMenuItem(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .clip(
                                    RoundedCornerShape(CORNER_RADIUS_CONTAINER)
                                ),
                            contentPadding = MENU_ITEM_PADDING,
                            text = {
                                Text(
                                    text = petTypeName,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            },
                            onClick = {
                                selectedType.value = petType
                                selectedTypeName.value = petTypeName
                                expanded.value = false
                                isError.value = false
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun EnterPetNameTextField(
    modifier: Modifier = Modifier,
    petName: MutableState<String>,
    isError: MutableState<Boolean>
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = petName.value,
        onValueChange = { value ->
            petName.value = value.filter { symbol -> symbol.isLetterOrDigit() }
            // TODO: при повторном вводе пустой строки не появляется красная обводка
            isError.value = false
        },
        placeholder = {
            Text(stringResource(id = R.string.pet_name_hint))
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        shape = RoundedCornerShape(CORNER_RADIUS_CONTAINER),
        singleLine = true,
        isError = isError.value,
        supportingText = {
            if (isError.value) {
                Text(text = stringResource(id = R.string.error_pet_name))
            }
        },
        trailingIcon = {
            if (isError.value) {
                Icon(imageVector = Icons.Rounded.Warning, contentDescription = null)
            }
        },
    )
}