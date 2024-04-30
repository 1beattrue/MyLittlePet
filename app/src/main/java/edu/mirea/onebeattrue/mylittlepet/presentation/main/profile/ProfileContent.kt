package edu.mirea.onebeattrue.mylittlepet.presentation.main.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    component: ProfileComponent
) {
    Column {
        Text(text = "profile component")
        Button(onClick = { component.signOut() }) {
            Text(text = "Log out")
        }
    }
}