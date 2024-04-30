package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.editpet

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultEditPetComponent @AssistedInject constructor(
    @Assisted("componentContext") componentContext: ComponentContext
) : EditPetComponent, ComponentContext by componentContext {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultEditPetComponent
    }
}