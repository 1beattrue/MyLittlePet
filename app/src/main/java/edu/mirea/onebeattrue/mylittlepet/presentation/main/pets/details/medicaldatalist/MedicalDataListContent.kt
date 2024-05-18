package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.medicaldatalist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.rounded.Add
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
import androidx.compose.ui.unit.dp
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalData
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCardExtremeElevation
import edu.mirea.onebeattrue.mylittlepet.ui.customview.CustomCardWithAddButton
import edu.mirea.onebeattrue.mylittlepet.ui.theme.CORNER_RADIUS_SURFACE

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MedicalDataListContent(
    modifier: Modifier = Modifier,
    component: MedicalDataListComponent
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
                        text = stringResource(R.string.medical_data_list_app_bar_title)
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
                        onClick = { component.onAddMedicalData() }
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
                items(
                    items = state.medicalDataList,
                    key = { it.id },
                ) { medicalData ->
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
                                component.onDeleteMedicalData(medicalData)
                                swipeState.snapTo(SwipeToDismissBoxValue.Settled)
                            }
                        }

                        SwipeToDismissBoxValue.EndToStart -> {
                            LaunchedEffect(Any()) {
                                component.onDeleteMedicalData(medicalData)
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
                        MedicalDataCard(
                            medicalData = medicalData
                        )
                    }
                }
                item {
                    CustomCardWithAddButton(
                        onAddClick = { component.onAddMedicalData() }
                    ) {
                        Text(
                            text = stringResource(R.string.add_medical_data),
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MedicalDataCard(
    modifier: Modifier = Modifier,
    medicalData: MedicalData
) {
    CustomCardExtremeElevation(
        modifier = modifier,
    ) {
        // TODO: отрисовать ui
    }
}