package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.editpet

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.PetType
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.image.DefaultImageComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.name.DefaultNameComponent
import kotlinx.serialization.Serializable

class DefaultEditPetComponent @AssistedInject constructor(
    private val nameComponentFactory: DefaultNameComponent.Factory,
    private val imageComponentFactory: DefaultImageComponent.Factory,

    @Assisted("petType") private val petType: PetType,
    @Assisted("onEditPetClosed") private val onEditPetClosed: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : EditPetComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, EditPetComponent.Child>> = childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Name(petType = petType),
            handleBackButton = true,
            childFactory = ::child,
            key = "edit_pet"
        )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): EditPetComponent.Child = when (config) {

        is Config.Name -> {
            val component = nameComponentFactory.create(
                petType = config.petType,
                onNextClicked = { petType, petName ->
                    navigation.push(Config.Image(petType, petName))
                },
                componentContext = componentContext
            )
            EditPetComponent.Child.Name(component)
        }

        is Config.Image -> {
            val component = imageComponentFactory.create(
                petType = config.petType,
                petName = config.petName,
                onAddPetClosed = {
                    onEditPetClosed()
                },
                componentContext = componentContext
            )
            EditPetComponent.Child.Image(component)
        }
    }


    override fun onBackClicked() {
        onEditPetClosed()
    }

    @Serializable
    sealed interface Config {

        @Serializable
        data class Name(val petType: PetType) : Config

        @Serializable
        data class Image(val petType: PetType, val petName: String) : Config
    }


    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("petType") petType: PetType,
            @Assisted("onEditPetClosed") onEditPetClosed: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultEditPetComponent
    }
}