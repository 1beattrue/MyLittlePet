package edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.petinfo

import com.arkivanov.decompose.ComponentContext
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.AddPetUseCase
import edu.mirea.onebeattrue.mylittlepet.presentation.utils.componentScope
import kotlinx.coroutines.launch

class DefaultPetInfoComponent @AssistedInject constructor(
    private val addPetUseCase: AddPetUseCase,

    @Assisted("onBackClicked") private val onBackClicked: () -> Unit,
    @Assisted("petString") private val petString: String,
    @Assisted("componentContext") componentContext: ComponentContext
) : PetInfoComponent, ComponentContext by componentContext {

    override val pet: Pet? = try {
        Gson().fromJson(petString, Pet::class.java)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    override fun onClickBack() {
        onBackClicked()
    }

    override fun addPet() {
        componentScope.launch {
            pet?.let {
                addPetUseCase(it.copy(id = Pet.UNDEFINED_ID))
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onBackClicked") onBackClicked: () -> Unit,
            @Assisted("petString") petString: String,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultPetInfoComponent
    }
}