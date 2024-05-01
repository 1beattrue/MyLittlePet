package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultAddPetComponent @AssistedInject constructor(

    @Assisted("componentContext") componentContext: ComponentContext
) : AddPetComponent, ComponentContext by componentContext {

    override val stack: Value<ChildStack<*, AddPetComponent.Child>>
        get() = TODO("Not yet implemented")

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultAddPetComponent
    }
}