package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addmedicaldata.photo

import android.net.Uri
import kotlinx.coroutines.flow.StateFlow

interface MedicalPhotoComponent {

    val model: StateFlow<MedicalPhotoStore.State>
    fun setPhoto(imageUri: Uri)
    fun finish()
    fun deletePhoto()
}