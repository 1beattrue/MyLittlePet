package edu.mirea.onebeattrue.mylittlepet.ui.customview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.mirea.onebeattrue.mylittlepet.ui.theme.ROUNDED_CORNER_SIZE_SURFACE
import edu.mirea.onebeattrue.mylittlepet.ui.theme.STRONG_ELEVATION
import edu.mirea.onebeattrue.mylittlepet.ui.theme.DEFAULT_ELEVATION
import edu.mirea.onebeattrue.mylittlepet.ui.theme.EXTREME_ELEVATION

@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(ROUNDED_CORNER_SIZE_SURFACE),
        elevation = CardDefaults.cardElevation(defaultElevation = DEFAULT_ELEVATION)
    ) {
        Column(content = content)
    }
}

@Composable
fun CustomCardStrongElevation(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(ROUNDED_CORNER_SIZE_SURFACE),
        elevation = CardDefaults.cardElevation(defaultElevation = STRONG_ELEVATION)
    ) {
        Column(content = content)
    }
}

@Composable
fun CustomCardExtremeElevation(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(ROUNDED_CORNER_SIZE_SURFACE),
        elevation = CardDefaults.cardElevation(defaultElevation = EXTREME_ELEVATION)
    ) {
        Column(content = content)
    }
}