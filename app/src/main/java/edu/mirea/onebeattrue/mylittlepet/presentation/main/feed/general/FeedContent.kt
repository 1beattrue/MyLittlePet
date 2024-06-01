package edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.general

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FeedContent(
    modifier: Modifier = Modifier,
    component: FeedComponent
) {
    Button(onClick = { component.openScanner() }) {
        Text(text = "Scan")
    }
}