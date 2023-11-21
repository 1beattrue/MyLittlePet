package edu.mirea.onebeattrue.mylittlepet.presentation.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.mirea.onebeattrue.mylittlepet.presentation.viewmodels.ViewModelFactory
import edu.mirea.onebeattrue.mylittlepet.presentation.viewmodels.auth.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModelFactory: ViewModelFactory,
    signOut: () -> Unit
) {
    val viewModel: ProfileViewModel = viewModel(factory = viewModelFactory)
    Column {
        Text(text = "Profile Screen")
        Button(
            onClick = { signOut() }
        ) {
            Text(text = "Выход")
        }
    }
}