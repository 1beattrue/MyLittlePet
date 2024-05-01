package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.DefaultAddPetComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.DefaultDetailsComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.editpet.DefaultEditPetComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.petlist.DefaultPetListComponent
import kotlinx.serialization.Serializable

class DefaultPetsComponent @AssistedInject constructor(
    private val petListComponentFactory: DefaultPetListComponent.Factory,
    private val addPetComponentFactory: DefaultAddPetComponent.Factory,
    private val editPetComponentFactory: DefaultEditPetComponent.Factory,
    private val detailsComponentFactory: DefaultDetailsComponent.Factory,

    @Assisted("componentContext") componentContext: ComponentContext
) : PetsComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, PetsComponent.Child>>
        get() = childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.PetList,
            handleBackButton = true,
            childFactory = ::child,
            key = "pets"
        )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): PetsComponent.Child = when (config) {
        Config.AddPet -> {
            val component = addPetComponentFactory.create(
                componentContext = componentContext,
                onAddPetClicked = {
                    navigation.pop()
                }
            )
            PetsComponent.Child.AddPet(component)
        }

        Config.Details -> {
            val component = detailsComponentFactory.create(
                componentContext = componentContext
            )
            PetsComponent.Child.Details(component)
        }

        Config.EditPet -> {
            val component = editPetComponentFactory.create(
                componentContext = componentContext
            )
            PetsComponent.Child.EditPet(component)
        }

        Config.PetList -> {
            val component = petListComponentFactory.create(
                onAddPetClicked = {
                    navigation.push(Config.AddPet)
                },
                onEditPetClicked = {
                    navigation.push(Config.EditPet)
                },
                componentContext = componentContext
            )
            PetsComponent.Child.PetList(component)
        }
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object PetList : Config

        @Serializable
        data object AddPet : Config

        @Serializable
        data object EditPet : Config

        @Serializable
        data object Details : Config
    }

    @AssistedFactory
    interface Factory {
        fun create(

            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultPetsComponent
    }
}