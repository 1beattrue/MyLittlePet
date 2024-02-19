package edu.mirea.onebeattrue.mylittlepet.presentation.pets

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.mirea.onebeattrue.mylittlepet.R
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.PetType
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.AddPetUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddPetViewModel @Inject constructor(
    private val application: Application,
    private val addPetUseCase: AddPetUseCase
) : ViewModel() {
    private val _screenState =
        MutableStateFlow<AddPetScreenState>(AddPetScreenState.Initial)
    val screenState = _screenState.asStateFlow()

    init {
        _screenState.value = AddPetScreenState.SelectPetType
    }

    fun addPet(pet: Pet) {
        viewModelScope.launch(Dispatchers.IO) {
            addPetUseCase(pet)
            _screenState.value = AddPetScreenState.Success
        }
    }

    fun moveToEnterName(petType: PetType?) {
        if (petType == null) {
            _screenState.value = AddPetScreenState.Failure(application.getString(R.string.error_pet_type))
        } else {
            _screenState.value = AddPetScreenState.SelectPetName
        }
    }

    fun moveToSelectImage(petName: String) {
        if (petName.isBlank()) {
            Log.d("AddPetViewModel", "Failure")
            _screenState.value = AddPetScreenState.Failure(application.getString(R.string.error_pet_name))
        } else {
            Log.d("AddPetViewModel", "SelectPetImage")
            _screenState.value = AddPetScreenState.SelectPetImage
        }
    }
}