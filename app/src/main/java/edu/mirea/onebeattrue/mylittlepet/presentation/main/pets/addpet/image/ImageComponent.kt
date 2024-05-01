package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.image

import android.net.Uri
import kotlinx.coroutines.flow.StateFlow

interface ImageComponent {
    val model: StateFlow<ImageStore.State>

    fun setPetImage(imageUri: Uri)

    fun addPet()
}