package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addnote

import androidx.compose.runtime.Composable
import edu.mirea.onebeattrue.mylittlepet.R

enum class NoteIcon(
    val iconResId: Int
) {
    FoodItem(R.drawable.ic_food),
    PlayItem(R.drawable.ic_play),
    MedicineItem(R.drawable.ic_medicine);

    companion object {
        @Composable
        fun getItems(): List<NoteIcon> = NoteIcon.entries
    }
}