package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultPetsComponent @AssistedInject constructor(
    @Assisted("componentContext") componentContext: ComponentContext
) : PetsComponent, ComponentContext by componentContext {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultPetsComponent
    }
}