package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PetsContent(
    modifier: Modifier = Modifier,
    component: PetsComponent
) {
    Column {
        Text(text = "pets component")
    }
}