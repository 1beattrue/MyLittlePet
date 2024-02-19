package edu.mirea.onebeattrue.mylittlepet.presentation.pets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.PetType
import edu.mirea.onebeattrue.mylittlepet.extensions.getImageId
import edu.mirea.onebeattrue.mylittlepet.extensions.getName
import edu.mirea.onebeattrue.mylittlepet.presentation.ViewModelFactory
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomBackButton
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCardDefaultElevation
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomNextButton
import edu.mirea.onebeattrue.mylittlepet.ui.theme.CORNER_RADIUS_CONTAINER
import edu.mirea.onebeattrue.mylittlepet.ui.theme.MENU_ITEM_PADDING

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetScreen(
    modifier: Modifier = Modifier,
    viewModelFactory: ViewModelFactory,
    closeScreen: () -> Unit
) {
    // states --------------------------------------------------------------------------------------
    val initialSelect = stringResource(id = R.string.initial_pet_type)
    var selectedTypeName by rememberSaveable { mutableStateOf(initialSelect) }

    var selectedType by rememberSaveable { mutableStateOf<PetType?>(null) }

    var expanded by rememberSaveable { mutableStateOf(false) }

    var isTextFieldError by rememberSaveable { mutableStateOf(false) }

    var supportingText by rememberSaveable { mutableStateOf("") }

    var isBackButtonVisible by rememberSaveable { mutableStateOf(false) }


    val viewModel: AddPetViewModel = viewModel(factory = viewModelFactory)

    val petTypes = PetType.getTypes()

    val addPetScreenState by viewModel.screenState.collectAsState(
        AddPetScreenState.Initial
    )

    when (val screenState = addPetScreenState) {
        is AddPetScreenState.Failure -> {
            isTextFieldError = true
            supportingText = screenState.message
        }

        AddPetScreenState.SelectPetType -> {
            isTextFieldError = false
            isBackButtonVisible = false
        }

        AddPetScreenState.SelectPetName -> {
            isTextFieldError = false
            isBackButtonVisible = true
        }

        AddPetScreenState.SelectPetImage -> {
            isTextFieldError = false
            isBackButtonVisible = true
        }

        AddPetScreenState.Initial -> {}
    }

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
            CustomCardDefaultElevation {
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
                            shape = RoundedCornerShape(CORNER_RADIUS_CONTAINER),
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                if (isTextFieldError) {
                                    Icon(imageVector = Icons.Rounded.Warning, contentDescription = null)
                                }
                                 },
                            isError = isTextFieldError,
                            supportingText = {
                                if (isTextFieldError) Text(text = supportingText)
                            },
                            leadingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            }
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
                                            RoundedCornerShape(CORNER_RADIUS_CONTAINER)
                                        ),
                                    contentPadding = MENU_ITEM_PADDING,
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
                                        isTextFieldError = false
                                    }
                                )
                            }
                        }
                    }
                }
                Box {
                    CustomNextButton(
                        onClick = {
                            viewModel.moveToEnterName(petType = selectedType)
                        }
                    )
                    Row {
                        AnimatedVisibility(
                            visible = isBackButtonVisible,
                            enter = fadeIn(),
                            exit = fadeOut(),
                        ) {
                            CustomBackButton(
                                onClick = { closeScreen() },
                            )
                        }
                    }
                }
            }
        }
    }
}
