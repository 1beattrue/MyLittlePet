package edu.mirea.onebeattrue.mylittlepet.presentation.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.AuthComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.DefaultAuthComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.DefaultPetsComponent
import kotlinx.parcelize.Parcelize

class DefaultMainComponent @AssistedInject constructor(
    private val petsComponentFactory: DefaultPetsComponent.Factory,

    @Assisted("componentContext") componentContext: ComponentContext
) : MainComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, MainComponent.Child>>
        get() = childStack(
            source = navigation,
            initialConfiguration = Config.Pets,
            handleBackButton = true,
            childFactory = ::child
        )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): MainComponent.Child = when (config) {
        Config.Pets -> {
            val component = petsComponentFactory.create(componentContext)
            MainComponent.Child.Pets(component)
        }
    }

    sealed interface Config : Parcelable {
        @Parcelize
        data object Pets : Config
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultMainComponent
    }
}