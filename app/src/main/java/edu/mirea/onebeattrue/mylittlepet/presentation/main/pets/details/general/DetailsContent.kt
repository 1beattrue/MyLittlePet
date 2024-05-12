package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.general

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Event
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.extensions.convertMillisToLocalDate
import edu.mirea.onebeattrue.mylittlepet.extensions.getImageId
import edu.mirea.onebeattrue.mylittlepet.extensions.getName
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCard
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCardExtremeElevation
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCardWithAddButton
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomReadyButton
import edu.mirea.onebeattrue.mylittlepet.ui.theme.CORNER_RADIUS_CONTAINER
import edu.mirea.onebeattrue.mylittlepet.ui.theme.CORNER_RADIUS_SURFACE
import edu.mirea.onebeattrue.mylittlepet.ui.theme.EXTREME_ELEVATION
import edu.mirea.onebeattrue.mylittlepet.ui.theme.STRONG_ELEVATION
import kotlinx.coroutines.launch


@Composable
fun DetailsContent(
    modifier: Modifier = Modifier,
    component: DetailsComponent
) {
    val state by component.model.collectAsState()
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 16.dp),
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

        eventList(
            eventList = state.event.list,
            onAddEvent = {
                component.onAddEventClick()
            },
            onDeleteEvent = { event ->
                component.onDeleteEvent(event)
            },
        )
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
            component.onWeightChages(weight)
        },
        onSetWeight = { component.setWeight() },
        mustBeClosed = state.bottomSheetMustBeClosed
    )
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
                    text = stringResource(id = R.string.enter_weight),
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
    CustomCardExtremeElevation {
        Text(
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            text = pet.name,
            fontWeight = FontWeight.Bold,
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
                    model = pet.imageUri,
                    contentDescription = null
                )
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
        CustomCard(
            elevation = EXTREME_ELEVATION,
            paddingValues = PaddingValues(start = 16.dp, end = 8.dp),
            onClick = { onAgeClicked() }
        ) {
            Text(
                text = stringResource(R.string.age_title),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            val formattedAge = if (age.years == null || age.months == null) {
                stringResource(R.string.unknown_age)
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
                textAlign = TextAlign.Center
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
        CustomCard(
            elevation = EXTREME_ELEVATION,
            paddingValues = PaddingValues(start = 8.dp, end = 16.dp),
            onClick = { onWeightClicked() }
        ) {
            Text(
                text = stringResource(R.string.weight_title),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            val displayWeight = if (weight == null) {
                stringResource(R.string.unknown_weight)
            } else {
                stringResource(R.string.weight_format, weight)
            }

            Text(
                text = displayWeight,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomDatePickerDialog(
    modifier: Modifier = Modifier,
    state: Boolean,
    onDismissRequest: () -> Unit,
    onDatePicked: (Long) -> Unit
) {
    if (state) {
        val datePickerState = rememberDatePickerState()
        val confirmEnabled by derivedStateOf { datePickerState.selectedDateMillis != null }

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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
private fun LazyListScope.eventList(
    modifier: Modifier = Modifier,
    eventList: List<Event>,
    onAddEvent: () -> Unit,
    onDeleteEvent: (Event) -> Unit,
) {
    item {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.event_list_title),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
    }
    items(
        items = eventList,
        key = { it.id }
    ) { event ->
        val swipeState = rememberSwipeToDismissBoxState()

        lateinit var icon: ImageVector
        lateinit var alignment: Alignment
        val color: Color

        when (swipeState.dismissDirection) {
            SwipeToDismissBoxValue.StartToEnd -> {
                icon = Icons.Outlined.Delete
                alignment = Alignment.CenterEnd
                color = MaterialTheme.colorScheme.errorContainer
            }

            SwipeToDismissBoxValue.EndToStart -> {
                icon = Icons.Outlined.Delete
                alignment = Alignment.CenterEnd
                color = MaterialTheme.colorScheme.errorContainer
            }

            SwipeToDismissBoxValue.Settled -> {
                icon = Icons.Outlined.Delete
                alignment = Alignment.CenterEnd
                color = MaterialTheme.colorScheme.errorContainer
            }
        }

        when (swipeState.currentValue) {
            SwipeToDismissBoxValue.StartToEnd -> {
                LaunchedEffect(Any()) {
                    onDeleteEvent(event)
                    swipeState.snapTo(SwipeToDismissBoxValue.Settled)
                }
            }

            SwipeToDismissBoxValue.EndToStart -> {
                LaunchedEffect(Any()) {
                    onDeleteEvent(event)
                    swipeState.snapTo(SwipeToDismissBoxValue.Settled)
                }
            }

            SwipeToDismissBoxValue.Settled -> {
            }
        }

        SwipeToDismissBox(
            modifier = Modifier.animateItemPlacement(),
            state = swipeState,
            enableDismissFromStartToEnd = false,
            backgroundContent = {
                Box(
                    contentAlignment = alignment,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(CORNER_RADIUS_SURFACE))
                        .background(color)
                ) {
                    Icon(
                        modifier = Modifier.minimumInteractiveComponentSize(),
                        imageVector = icon, contentDescription = null
                    )
                }
            }
        ) {
            EventCard(
                event = event
            )
        }
    }
    item {
        CustomCardWithAddButton(
            elevation = 0.dp,
            onAddClick = { onAddEvent() }
        ) {
            Text(
                text = stringResource(R.string.add_event),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun EventCard(
    modifier: Modifier = Modifier,
    event: Event
) {
    CustomCard(
        elevation = 0.dp
    ) {
        Text(
            text = event.label,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )

        var formattedTime = "${
            event.hours.toString().padStart(2, '0')
        }:${
            event.minutes.toString().padStart(2, '0')
        }"

        event.date?.let {
            val localDate = it.convertMillisToLocalDate()
            val day = localDate.dayOfMonth
            val month = localDate.month.getName()
            val year = localDate.year
            formattedTime += ", ${stringResource(R.string.date_format, day, month, year)}"
        }

        Text(
            text = formattedTime,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}