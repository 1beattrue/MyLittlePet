package edu.mirea.onebeattrue.mylittlepet.presentation.pets

sealed class AddPetScreenState {
    object Initial : AddPetScreenState()

    data class SelectPetType(val isInvalidType: Boolean = false) : AddPetScreenState()

    data class SelectPetName(val isInvalidName: Boolean = false) : AddPetScreenState()

    object SelectPetImage : AddPetScreenState()

    object Success : AddPetScreenState()
}