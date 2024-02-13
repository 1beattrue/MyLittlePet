package edu.mirea.onebeattrue.mylittlepet.presentation.pets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.AddPetUseCase
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.DeletePetUseCase
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.EditPetUseCase
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.GetPetListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PetsViewModel @Inject constructor(
    private val addPetUseCase: AddPetUseCase,
    private val deletePetUseCase: DeletePetUseCase,
    private val editPetUseCase: EditPetUseCase,
    private val getPetListUseCase: GetPetListUseCase
) : ViewModel() {
    val pets: Flow<List<Pet>> = getPetListUseCase()

    fun addPet(pet: Pet) {
        viewModelScope.launch(Dispatchers.IO) {
            addPetUseCase(pet)
        }
    }

    fun deletePet(pet: Pet) {
        viewModelScope.launch(Dispatchers.IO) {
            deletePetUseCase(pet)
        }
    }

    fun editPet(pet: Pet) {
        viewModelScope.launch(Dispatchers.IO) {
            editPetUseCase(pet)
        }
    }
}