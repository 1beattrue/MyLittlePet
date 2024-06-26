package edu.mirea.onebeattrue.mylittlepet.ui.customview

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import edu.mirea.onebeattrue.mylittlepet.ui.theme.CORNER_RADIUS_SURFACE
import edu.mirea.onebeattrue.mylittlepet.ui.theme.EXTREME_ELEVATION


@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    elevation: Dp,
    paddingValues: PaddingValues = PaddingValues(horizontal = 16.dp),
    innerPadding: PaddingValues = PaddingValues(32.dp),
    cardColors: CardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(surfaceTint = Color(0x00FFFFFF))
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(paddingValues),
            colors = cardColors,
            shape = RoundedCornerShape(CORNER_RADIUS_SURFACE),
            elevation = CardDefaults.cardElevation(
                defaultElevation = elevation,
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
            }
        }
    }
}

private fun Float.formattedPadding(): Float = if (this < 0) 0f else this

@Composable
fun ClickableCustomCard(
    modifier: Modifier = Modifier,
    elevation: Dp,
    paddingValues: PaddingValues = PaddingValues(horizontal = 16.dp),
    innerPadding: PaddingValues = PaddingValues(32.dp),
    cardColors: CardColors = CardDefaults.elevatedCardColors(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ),
    onClick: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val pressedPadding by remember {
        mutableStateOf(Animatable(if (pressed) 4f else 0f))
    }

    LaunchedEffect(pressed) {
        pressedPadding.animateTo(
            targetValue = if (pressed) 4f else 0f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(surfaceTint = Color(0x00FFFFFF))
    ) {
        ElevatedCard(
            modifier = modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(pressedPadding.value.formattedPadding().dp),
            colors = cardColors,
            shape = RoundedCornerShape(CORNER_RADIUS_SURFACE),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = elevation,
                pressedElevation = elevation / 4
            ),
            onClick = {
                onClick()
            },
            interactionSource = interactionSource
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
            }
        }
    }
}

@Composable
fun CustomCardExtremeElevation(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    CustomCard(
        modifier = modifier,
        content = { content() },
        elevation = EXTREME_ELEVATION
    )
}

@Composable
fun CustomCardWithAddButton(
    modifier: Modifier = Modifier,
    elevation: Dp = EXTREME_ELEVATION,
    paddingValues: PaddingValues = PaddingValues(horizontal = 16.dp),
    onAddClick: () -> Unit,
    content: @Composable () -> Unit,
) {

    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(surfaceTint = Color(0x00FFFFFF))
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(paddingValues),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(CORNER_RADIUS_SURFACE),
            elevation = CardDefaults.cardElevation(defaultElevation = elevation)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    content()
                }

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.primary
                )

                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onAddClick() },
                    shape = CutCornerShape(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null
                    )
                }
            }
        }
    }
}