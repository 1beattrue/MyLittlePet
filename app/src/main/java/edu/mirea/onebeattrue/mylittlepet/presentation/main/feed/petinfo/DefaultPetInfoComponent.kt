package edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.petinfo

import com.arkivanov.decompose.ComponentContext
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet

class DefaultPetInfoComponent @AssistedInject constructor(
    @Assisted("petString") private val petString: String,
    @Assisted("componentContext") componentContext: ComponentContext
) : PetInfoComponent, ComponentContext by componentContext {

    override val pet: Pet
        get() = Gson().fromJson(petString, Pet::class.java)

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("petString") petString: String,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultPetInfoComponent
    }
}