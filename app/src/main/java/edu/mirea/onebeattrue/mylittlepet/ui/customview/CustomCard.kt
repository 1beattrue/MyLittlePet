package edu.mirea.onebeattrue.mylittlepet.ui.customview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import edu.mirea.onebeattrue.mylittlepet.ui.theme.CORNER_RADIUS_SURFACE
import edu.mirea.onebeattrue.mylittlepet.ui.theme.DEFAULT_ELEVATION
import edu.mirea.onebeattrue.mylittlepet.ui.theme.EXTREME_ELEVATION
import edu.mirea.onebeattrue.mylittlepet.ui.theme.STRONG_ELEVATION

@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    elevation: Dp,
    paddingValues: PaddingValues = PaddingValues(horizontal = 16.dp),
    onClick: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) {
            darkColorScheme()
        } else {
            lightColorScheme(surface = Color.White, surfaceTint = Color.White)
        }
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
                modifier = Modifier
                    .clickable {
                        onClick()
                    }
                    .fillMaxWidth()
                    .padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
            }
        }
    }
}

@Composable
fun CustomCardDefaultElevation(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    CustomCard(
        modifier = modifier,
        content = { content() },
        elevation = DEFAULT_ELEVATION
    )

}

@Composable
fun CustomCardStrongElevation(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    CustomCard(
        modifier = modifier,
        content = { content() },
        elevation = STRONG_ELEVATION
    )
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