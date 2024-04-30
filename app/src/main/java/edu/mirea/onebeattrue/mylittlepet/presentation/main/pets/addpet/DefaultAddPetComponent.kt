package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultAddPetComponent @AssistedInject constructor(
    @Assisted("componentContext") componentContext: ComponentContext
) : AddPetComponent, ComponentContext by componentContext {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultAddPetComponent
    }
}