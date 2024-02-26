package edu.mirea.onebeattrue.mylittlepet.presentation.pets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.PetType
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.AddPetUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddPetViewModel @Inject constructor(
    private val addPetUseCase: AddPetUseCase
) : ViewModel() {
    private val _screenState = MutableStateFlow<AddPetScreenState>(
        AddPetScreenState.Initial
    )
    val screenState = _screenState.asStateFlow()

    init {
        _screenState.value = AddPetScreenState.SelectPetType()
    }

    fun addPet(pet: Pet) {
        viewModelScope.launch(Dispatchers.IO) {
            addPetUseCase(pet)
            _screenState.value = AddPetScreenState.Success
        }
    }

    fun moveNextToEnterName(petType: PetType?) {
        if (petType == null) {
            _screenState.value = AddPetScreenState.SelectPetType(isInvalidType = true)
        } else {
            _screenState.value = AddPetScreenState.SelectPetName()
        }
    }

    fun moveNextToSelectImage(petName: String) {
        if (petName.isBlank()) {
            _screenState.value = AddPetScreenState.SelectPetName(isInvalidName = true)
        } else {
            _screenState.value = AddPetScreenState.SelectPetImage
        }
    }

    fun moveBackToSelectName() {
        _screenState.value = AddPetScreenState.SelectPetName()
    }

    fun moveBackToSelectType() {
        _screenState.value = AddPetScreenState.SelectPetType()
    }
}