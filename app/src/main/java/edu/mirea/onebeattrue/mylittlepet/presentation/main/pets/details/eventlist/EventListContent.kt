package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.eventlist

import android.Manifest
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.NotificationImportant
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.NotificationsOff
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Event
import edu.mirea.onebeattrue.mylittlepet.extensions.convertMillisToLocalDateTime
import edu.mirea.onebeattrue.mylittlepet.extensions.getName
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCard
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCardExtremeElevation
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCardWithAddButton
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomTextButton
import edu.mirea.onebeattrue.mylittlepet.ui.customview.ErrorCustomCard
import edu.mirea.onebeattrue.mylittlepet.ui.customview.ErrorCustomCardWithRetryButton
import edu.mirea.onebeattrue.mylittlepet.ui.theme.CORNER_RADIUS_SURFACE
import edu.mirea.onebeattrue.mylittlepet.ui.theme.DEFAULT_ELEVATION
import edu.mirea.onebeattrue.mylittlepet.ui.theme.EXTREME_ELEVATION
import java.util.Calendar

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun EventListContent(
    modifier: Modifier = Modifier,
    component: EventListComponent
) {
    val state by component.model.collectAsState()

    val permissionState =
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

    LaunchedEffect(key1 = Unit) {
        permissionState.launchPermissionRequest()
    }

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
                        text = stringResource(R.string.event_list_app_bar_title)
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
                        onClick = { component.onAddEvent() }
                    ) {
                        Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
                    }
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                if (state.isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItemPlacement(),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            CircularProgressIndicator()
                        }

                    }
                }

                if (state.syncError) {
                    item {
                        ErrorCustomCardWithRetryButton(
                            modifier = Modifier.animateItemPlacement(),
                            message = stringResource(R.string.synchronization_error)
                        ) {
                            component.syncronize()
                        }
                    }
                }

                item {
                    NotificationPermissionCard(
                        permissionState = permissionState
                    )
                }

                item {
                    DeletePastEventsCard(
                        modifier = Modifier.animateItemPlacement(),
                        deleteIrrelevantEventsError = state.deleteIrrelevantEventsError
                    ) {
                        component.onDeletePastEvents()
                    }
                }

//                if (state.deleteIrrelevantEventsError) {
//                    item {
//                        ErrorCustomCard(
//                            modifier = Modifier.padding(horizontal = 16.dp),
//                            message = stringResource(R.string.error_deleting_irrelevant_events)
//                        )
//                    }
//                }

                items(
                    items = state.eventList,
                    key = { it.id },
                ) { event ->
                    val swipeState = rememberSwipeToDismissBoxState()

                    val isRelevant = event.repeatable || isEventRelevant(event)

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
                                component.onDeleteEvent(event)
                                swipeState.snapTo(SwipeToDismissBoxValue.Settled)
                            }
                        }

                        SwipeToDismissBoxValue.EndToStart -> {
                            LaunchedEffect(Any()) {
                                component.onDeleteEvent(event)
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
                                    .padding(if (isRelevant) 0.dp else 4.dp)
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
                            event = event,
                            isDeleting = event.id == state.nowDeletingId,
                            deleteError = event.id == state.deleteEventErrorId,
                        )
                    }
                }
                item {
                    CustomCardWithAddButton(
                        onAddClick = { component.onAddEvent() }
                    ) {
                        Text(
                            text = stringResource(R.string.add_event),
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DeletePastEventsCard(
    modifier: Modifier = Modifier,
    deleteIrrelevantEventsError: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        AnimatedContent(
            targetState = deleteIrrelevantEventsError,
            label = "",
            transitionSpec = { fadeIn() togetherWith fadeOut() }
        ) { isError ->
            CustomTextButton(
                onClick = { onClick() },
                text = if (isError) {
                    stringResource(R.string.error_deleting_irrelevant_events)
                } else {
                    stringResource(R.string.clear_old_events)
                },
                icon = {
                    Icon(
                        imageVector = if (isError) {
                            Icons.Rounded.Refresh
                        } else {
                            Icons.Rounded.Delete
                        },
                        contentDescription = null
                    )
                },
                colors = if (isError) {
                    ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                } else {
                    ButtonDefaults.textButtonColors()
                }
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun NotificationPermissionCard(
    modifier: Modifier = Modifier,
    permissionState: PermissionState
) {
    CustomCardExtremeElevation(
        modifier = modifier
    ) {
        val (icon, text) = if (permissionState.status.isGranted) {
            Icons.Rounded.NotificationsActive to stringResource(R.string.notification_permission_granted)
        } else {
            if (permissionState.status.shouldShowRationale) {
                Icons.Rounded.NotificationImportant to stringResource(R.string.notification_permission_required)
            } else {
                Icons.Rounded.NotificationsOff to stringResource(R.string.notification_permission_denied)
            }
        }

        Icon(imageVector = icon, contentDescription = null)
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = text,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun EventCard(
    modifier: Modifier = Modifier,
    event: Event,
    deleteError: Boolean,
    isDeleting: Boolean,
) {
    val isRelevant = event.repeatable || isEventRelevant(event)

    val cardColors = getEventCardColors(
        isRelevant = isRelevant,
        isDeleting = isDeleting
    )

    CustomCard(
        modifier = modifier.padding(
            if (isRelevant) 0.dp else 4.dp
        ),
        elevation = if (isRelevant) EXTREME_ELEVATION else DEFAULT_ELEVATION,
        cardColors = cardColors
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = event.label,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Start
        )

        val localDateTime = event.time.convertMillisToLocalDateTime()
        val hour = localDateTime.hour
        val minute = localDateTime.minute

        var formattedTime = "${
            hour.toString().padStart(2, '0')
        }:${
            minute.toString().padStart(2, '0')
        }"

        if (!event.repeatable) {
            val day = localDateTime.dayOfMonth
            val month = localDateTime.month.getName()
            val year = localDateTime.year
            formattedTime += ", ${stringResource(R.string.date_format, day, month, year)}"
        }

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = formattedTime,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.End
        )

        AnimatedVisibility(
            visible = deleteError
        ) {
            ErrorCustomCard(
                message = stringResource(R.string.error_deleting_event)
            )
        }
    }
}

private fun isEventRelevant(event: Event): Boolean {
    val currentTimeMillis = Calendar.getInstance().timeInMillis
    return event.time > currentTimeMillis
}


@Composable
private fun getEventCardColors(
    isRelevant: Boolean,
    isDeleting: Boolean
): CardColors {
    if (isDeleting) {
        return CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        )
    } else if (isRelevant) {
        return CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    }
    return CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    )
}