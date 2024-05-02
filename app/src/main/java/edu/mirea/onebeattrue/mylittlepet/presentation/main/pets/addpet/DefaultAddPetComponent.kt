package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet

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
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.type.DefaultTypeComponent
import kotlinx.serialization.Serializable

class DefaultAddPetComponent @AssistedInject constructor(
    private val typeComponentFactory: DefaultTypeComponent.Factory,
    private val nameComponentFactory: DefaultNameComponent.Factory,
    private val imageComponentFactory: DefaultImageComponent.Factory,

    @Assisted("onAddPetClosed") private val onAddPetClosed: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : AddPetComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, AddPetComponent.Child>> = childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Type,
            handleBackButton = true,
            childFactory = ::child,
            key = "add_pet"
        )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): AddPetComponent.Child = when (config) {

        Config.Type -> {
            val component = typeComponentFactory.create(
                onNextClicked = { petType ->
                    navigation.push(Config.Name(petType))
                },
                componentContext = componentContext
            )
            AddPetComponent.Child.Type(component)
        }

        is Config.Name -> {
            val component = nameComponentFactory.create(
                petType = config.petType,
                onNextClicked = { petType, petName ->
                    navigation.push(Config.Image(petType, petName))
                },
                componentContext = componentContext
            )
            AddPetComponent.Child.Name(component)
        }

        is Config.Image -> {
            val component = imageComponentFactory.create(
                petType = config.petType,
                petName = config.petName,
                onAddPetClosed = {
                    onAddPetClosed()
                },
                componentContext = componentContext
            )
            AddPetComponent.Child.Image(component)
        }
    }

    override fun onBackClicked() {
        onAddPetClosed()
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object Type : Config

        @Serializable
        data class Name(val petType: PetType) : Config

        @Serializable
        data class Image(val petType: PetType, val petName: String) : Config
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onAddPetClosed") onAddPetClosed: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultAddPetComponent
    }
}