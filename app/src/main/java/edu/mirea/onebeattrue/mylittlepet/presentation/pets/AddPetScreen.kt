package edu.mirea.onebeattrue.mylittlepet.presentation.pets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.mirea.onebeattrue.mylittlepet.presentation.ViewModelFactory
import edu.mirea.onebeattrue.mylittlepet.ui.theme.ROUNDED_CORNER_SIZE_SURFACE
import edu.mirea.onebeattrue.mylittlepet.ui.theme.SMALL_ELEVATION

@Composable
fun AddPetScreen(
    modifier: Modifier = Modifier,
    viewModelFactory: ViewModelFactory,
    close: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(ROUNDED_CORNER_SIZE_SURFACE),
        elevation = CardDefaults.cardElevation(defaultElevation = SMALL_ELEVATION)
    ) {
        Button(onClick = { close() }) {
            Text(text = "close")
        }
    }
}