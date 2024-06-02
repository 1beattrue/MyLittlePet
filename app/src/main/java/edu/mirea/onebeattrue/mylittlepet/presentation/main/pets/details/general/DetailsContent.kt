package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.general

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.QrCode
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.extensions.getImageId
import edu.mirea.onebeattrue.mylittlepet.presentation.utils.ImageUtils
import edu.mirea.onebeattrue.mylittlepet.ui.customview.ClickableCustomCard
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCard
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomReadyButton
import edu.mirea.onebeattrue.mylittlepet.ui.theme.CORNER_RADIUS_CONTAINER
import edu.mirea.onebeattrue.mylittlepet.ui.theme.EXTREME_ELEVATION
import edu.mirea.onebeattrue.mylittlepet.ui.theme.STRONG_ELEVATION
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsContent(
    modifier: Modifier = Modifier,
    component: DetailsComponent
) {
    val state by component.model.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                title = {
                    Text(
                        style = MaterialTheme.typography.titleLarge,
                        text = stringResource(R.string.pet_details_app_bar_title)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { component.onBackClicked() },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            component.showQrCode()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.QrCode,
                            contentDescription = null
                        )
                    }
                }
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(
                top = 16.dp,
                bottom = 32.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                PetCard(pet = component.pet)
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AgeCard(
                        modifier = Modifier.weight(0.5f),
                        age = state.age
                    ) {
                        component.openDatePickerDialog()
                    }
                    WeightCard(
                        modifier = Modifier.weight(0.5f),
                        weight = state.weight.value
                    ) {
                        component.onChangeWeightClick()
                    }
                }
            }
            item {
                EventListCard {
                    component.onClickEventList()
                }
            }
            item {
                NoteListCard {
                    component.onClickNoteList()
                }
            }
            item {
                MedicalDataListCard {
                    component.onClickMedicalDataList()
                }
            }
        }
    }

    CustomDatePickerDialog(
        state = state.age.datePickerDialogState,
        onDismissRequest = { component.closeDatePickerDialog() },
        onDatePicked = { date ->
            component.setAge(date)
        }
    )

    WeightBottomSheet(
        isExpanded = state.weight.bottomSheetState,
        onCloseBottomSheet = { component.onCloseBottomSheetClick() },
        weightInput = state.weight.changeableValue,
        isError = state.weight.isIncorrect,
        onChangeWeight = { weight ->
            component.onWeightChanges(weight)
        },
        onSetWeight = { component.setWeight() },
        mustBeClosed = state.bottomSheetMustBeClosed
    )

    if (state.qrCode.isOpen) {
        QrCodeDialog(
            onDismissRequest = { component.hideQrCode() },
            qrCode = state.qrCode.bitmap
        )
    }

}

@Composable
private fun EventListCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ClickableCustomCard(
        modifier = modifier,
        elevation = EXTREME_ELEVATION,
        onClick = { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(0.7f),
                overflow = TextOverflow.Ellipsis,
                text = stringResource(R.string.event_list_app_bar_title),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start
            )
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun NoteListCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ClickableCustomCard(
        modifier = modifier,
        elevation = EXTREME_ELEVATION,
        onClick = { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(0.7f),
                overflow = TextOverflow.Ellipsis,
                text = stringResource(R.string.note_list_app_bar_title),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start,

                )
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun MedicalDataListCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ClickableCustomCard(
        modifier = modifier,
        elevation = EXTREME_ELEVATION,
        onClick = { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(0.7f),
                overflow = TextOverflow.Ellipsis,
                text = stringResource(R.string.medical_data_list_app_bar_title),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start
            )
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null
            )
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WeightBottomSheet(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    onCloseBottomSheet: () -> Unit,
    weightInput: String,
    isError: Boolean,
    onChangeWeight: (String) -> Unit,
    onSetWeight: () -> Unit,
    mustBeClosed: Boolean
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    scope.launch {
        if (mustBeClosed) {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    onCloseBottomSheet()
                }
            }
        }
    }

    if (isExpanded) {
        ModalBottomSheet(
            onDismissRequest = {
                onCloseBottomSheet()
            },
            sheetState = sheetState
        ) {
            CustomCard(elevation = STRONG_ELEVATION) {
                Text(
                    text = stringResource(id = R.string.enter_weight_hint),
                    style = MaterialTheme.typography.titleLarge
                )

                OutlinedTextField(
                    modifier = modifier.fillMaxWidth(),
                    value = weightInput,
                    onValueChange = {
                        onChangeWeight(it)
                    },
                    isError = isError,
                    supportingText = {
                        if (isError) {
                            Text(text = stringResource(id = R.string.error_weight))
                        }
                    },
                    trailingIcon = {
                        if (isError) {
                            Icon(
                                imageVector = Icons.Rounded.Warning,
                                contentDescription = null
                            )
                        }
                    },
                    shape = RoundedCornerShape(CORNER_RADIUS_CONTAINER),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = {
                        Text(stringResource(id = R.string.weight_hint))
                    },
                    suffix = {
                        Text(stringResource(R.string.weight_suffix))
                    }
                )

                CustomReadyButton(onClick = {
                    onSetWeight()
                })
            }

            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun PetCard(
    modifier: Modifier = Modifier,
    pet: Pet
) {
    ClickableCustomCard(
        modifier = modifier,
        elevation = EXTREME_ELEVATION
    ) {
        Text(
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            text = pet.name,
            fontWeight = FontWeight.Bold,
        )

        Box(
            modifier = Modifier.clip(RoundedCornerShape(CORNER_RADIUS_CONTAINER))
        ) {
            if (pet.image == null) {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(id = pet.type.getImageId()),
                    contentDescription = null
                )
            } else {
                ImageUtils.byteArrayToBitmap(pet.image)?.asImageBitmap()?.let {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        bitmap = it,
                        contentDescription = null
                    )
                }
            }
        }
    }
}


@Composable
private fun AgeCard(
    modifier: Modifier = Modifier,
    age: DetailsStore.State.AgeState,
    onAgeClicked: () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        ClickableCustomCard(
            elevation = EXTREME_ELEVATION,
            paddingValues = PaddingValues(start = 16.dp, end = 8.dp),
            onClick = { onAgeClicked() }
        ) {
            Text(
                text = stringResource(R.string.age_title),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            val formattedAge = if (age.years == null || age.months == null) {
                stringResource(R.string.enter_age)
            } else {
                val years = age.years
                val months = age.months

                val yearsString =
                    pluralStringResource(id = R.plurals.years_format, count = years, years)
                val monthsString =
                    pluralStringResource(id = R.plurals.months_format, count = months, months)

                if (years <= 0) {
                    monthsString
                } else if (months <= 0) {
                    yearsString
                } else {
                    "$yearsString $monthsString"
                }
            }

            Text(
                text = formattedAge,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun WeightCard(
    modifier: Modifier = Modifier,
    weight: Float?,
    onWeightClicked: () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        ClickableCustomCard(
            elevation = EXTREME_ELEVATION,
            paddingValues = PaddingValues(start = 8.dp, end = 16.dp),
            onClick = { onWeightClicked() }
        ) {
            Text(
                text = stringResource(R.string.weight_title),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            val displayWeight = if (weight == null) {
                stringResource(R.string.enter_weight)
            } else {
                stringResource(R.string.weight_format, weight)
            }

            Text(
                text = displayWeight,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomDatePickerDialog(
    state: Boolean,
    onDismissRequest: () -> Unit,
    onDatePicked: (Long) -> Unit
) {
    if (state) {
        val datePickerState = rememberDatePickerState()
        val confirmEnabled by derivedStateOf { datePickerState.selectedDateMillis != null }

        MaterialTheme(colorScheme = MaterialTheme.colorScheme.copy(surfaceTint = Color.White)) {
            DatePickerDialog(
                onDismissRequest = { onDismissRequest() },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onDatePicked(datePickerState.selectedDateMillis ?: 0)
                        },
                        enabled = confirmEnabled
                    ) {
                        Text(text = stringResource(id = R.string.ok))
                    }
                }
            ) {
                DatePicker(
                    title = {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium,
                            text = stringResource(R.string.date_picker_title),
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    state = datePickerState
                )
            }
        }
    }
}

@Composable
private fun QrCodeDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    qrCode: Bitmap?
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = { onDismissRequest() },
        title = {
            Text(
                text = stringResource(R.string.qr_code_title),
                textAlign = TextAlign.Center
            )
        },
        text = {
            if (qrCode != null) {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    bitmap = qrCode.asImageBitmap(),
                    contentDescription = null,
                )
            } else {
                Text(text = stringResource(R.string.something_went_wrong))
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(text = stringResource(R.string.ready))
            }
        }
    )
}