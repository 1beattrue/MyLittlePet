package edu.mirea.onebeattrue.mylittlepet.presentation.pets


sealed class AddPetScreenState {
    object Initial : AddPetScreenState()

    object SelectPetType : AddPetScreenState()

    object SelectPetName : AddPetScreenState()

    object SelectPetImage : AddPetScreenState()

    data class Failure(val message: String) : AddPetScreenState()
}