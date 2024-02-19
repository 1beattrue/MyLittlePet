package edu.mirea.onebeattrue.mylittlepet.presentation.pets

import android.app.Application
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
        }
    }

    fun moveToEnterName(petType: PetType?) {
        if (petType == null) {
            _screenState.value = AddPetScreenState.Failure(application.getString(R.string.pet_type_error))
        } else {
            _screenState.value = AddPetScreenState.SelectPetName
        }
    }
}