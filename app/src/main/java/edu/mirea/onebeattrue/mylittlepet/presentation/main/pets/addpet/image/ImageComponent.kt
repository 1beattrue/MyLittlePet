package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.image

import android.net.Uri
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.PetType
import kotlinx.coroutines.flow.StateFlow

interface ImageComponent {

    val model: StateFlow<ImageStore.State>

    val petType: PetType

    fun setPetImage(imageUri: Uri)

    fun finish()

    fun deletePetImage()
}