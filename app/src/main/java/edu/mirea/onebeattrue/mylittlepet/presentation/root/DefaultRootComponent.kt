package edu.mirea.onebeattrue.mylittlepet.presentation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.domain.auth.repository.AuthRepository
import edu.mirea.onebeattrue.mylittlepet.presentation.auth.DefaultAuthComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.DefaultMainComponent
import kotlinx.serialization.Serializable

class DefaultRootComponent @AssistedInject constructor(
    private val authComponentFactory: DefaultAuthComponent.Factory,
    private val mainComponentFactory: DefaultMainComponent.Factory,

    private val authRepository: AuthRepository,

    @Assisted("componentContext") componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>>
        get() = childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = if (authRepository.currentUser == null) Config.Auth else Config.Main,
            handleBackButton = true,
            childFactory = ::child,
            key = "root"
        )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child = when (config) {
        Config.Auth -> {
            val component = authComponentFactory.create(
                componentContext = componentContext,
                onLoggedIn = { navigation.replaceAll(Config.Main) }
            )
            RootComponent.Child.Auth(component)
        }

        Config.Main -> {
            val component = mainComponentFactory.create(
                componentContext = componentContext,
                onSignOutClicked = { navigation.replaceAll(Config.Auth) }
            )
            RootComponent.Child.Main(component)
        }
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Auth : Config

        @Serializable
        data object Main : Config
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultRootComponent
    }
}