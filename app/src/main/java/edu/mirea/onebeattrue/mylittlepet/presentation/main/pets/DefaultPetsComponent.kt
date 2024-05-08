package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.DefaultAddPetComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.DefaultDetailsRootComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.general.DefaultDetailsComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.editpet.DefaultEditPetComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.petlist.DefaultPetListComponent
import kotlinx.serialization.Serializable

class DefaultPetsComponent @AssistedInject constructor(
    private val petListComponentFactory: DefaultPetListComponent.Factory,
    private val addPetComponentFactory: DefaultAddPetComponent.Factory,
    private val editPetComponentFactory: DefaultEditPetComponent.Factory,
    private val detailsRootComponentFactory: DefaultDetailsRootComponent.Factory,

    @Assisted("onChangedBottomMenuVisibility") private val onChangedBottomMenuVisibility: (Boolean) -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : PetsComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    private val initialConfig = Config.PetList

    override val stack: Value<ChildStack<*, PetsComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = initialConfig,
        handleBackButton = true,
        childFactory = ::child,
        key = "pets"
    )

    init {
        stack.subscribe {
            val bottomBarVisibility = it.active.configuration == initialConfig
            onChangedBottomMenuVisibility(bottomBarVisibility)
        }
    }

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): PetsComponent.Child = when (config) {
        Config.AddPet -> {
            val component = addPetComponentFactory.create(
                componentContext = componentContext,
                onAddPetClosed = {
                    navigation.pop()
                }
            )
            PetsComponent.Child.AddPet(component)
        }

        is Config.Details -> {
            val component = detailsRootComponentFactory.create(
                componentContext = componentContext,
                pet = config.pet,
                onBackClick = {
                    navigation.pop()
                }
            )
            PetsComponent.Child.Details(component)
        }

        is Config.EditPet -> {
            val component = editPetComponentFactory.create(
                componentContext = componentContext,
                pet = config.pet,
                onEditPetClosed = {
                    navigation.pop()
                }
            )
            PetsComponent.Child.EditPet(component)
        }

        Config.PetList -> {
            val component = petListComponentFactory.create(
                onAddPetClicked = {
                    navigation.pushNew(Config.AddPet)
                },
                onEditPetClicked = { pet ->
                    navigation.pushNew(Config.EditPet(pet))
                },
                onOpenDetails = { pet ->
                    navigation.pushNew(Config.Details(pet))
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
        data class EditPet(val pet: Pet) : Config

        @Serializable
        data class Details(val pet: Pet) : Config
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onChangedBottomMenuVisibility") onChangedBottomMenuVisibility: (Boolean) -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultPetsComponent
    }
}