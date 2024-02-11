package edu.mirea.onebeattrue.mylittlepet.presentation.pets

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PetsScreen() {
    Column {
        Text(text = "Pets Screen")
        Text(text = FirebaseAuth.getInstance().currentUser.toString())
    }
}