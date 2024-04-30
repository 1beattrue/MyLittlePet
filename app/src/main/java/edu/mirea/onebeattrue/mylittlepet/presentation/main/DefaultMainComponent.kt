package edu.mirea.onebeattrue.mylittlepet.presentation.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.DefaultFeedComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.DefaultPetsComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.profile.DefaultProfileComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.parcelize.Parcelize

class DefaultMainComponent @AssistedInject constructor(
    private val storeFactory: MainStoreFactory,
    private val feedComponentFactory: DefaultFeedComponent.Factory,
    private val petsComponentFactory: DefaultPetsComponent.Factory,
    private val profileComponentFactory: DefaultProfileComponent.Factory,

    @Assisted("onSignOutClicked") private val onSignOutClicked: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext,
) : MainComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<MainStore.State>
        get() = store.stateFlow

    private val initialConfig = Config.Pets

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, MainComponent.Child>>
        get() = childStack(
            source = navigation,
            initialConfiguration = initialConfig,
            handleBackButton = false,
            childFactory = ::child,
            key = "main"
        )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): MainComponent.Child = when (config) {
        Config.Feed -> {
            val component = feedComponentFactory.create(
                componentContext = componentContext
            )
            MainComponent.Child.Feed(component)
        }

        Config.Pets -> {
            val component = petsComponentFactory.create(
                componentContext = componentContext
            )
            MainComponent.Child.Pets(component)
        }

        Config.Profile -> {
            val component = profileComponentFactory.create(
                componentContext = componentContext,
                onSignOutClicked = { onSignOutClicked() }
            )
            MainComponent.Child.Profile(component)
        }
    }

    override fun navigateTo(navigationItem: NavigationItem) {
        store.accept(MainStore.Intent.NavigateTo(navigationItem))
        when (navigationItem) {
            NavigationItem.FeedItem -> navigation.bringToFront(Config.Feed)
            NavigationItem.PetsItem -> navigation.bringToFront(Config.Pets)
            NavigationItem.ProfileItem -> navigation.bringToFront(Config.Profile)
        }

    }

    sealed interface Config : Parcelable {
        @Parcelize
        data object Feed : Config

        @Parcelize
        data object Pets : Config

        @Parcelize
        data object Profile : Config
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onSignOutClicked") onSignOutClicked: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultMainComponent
    }
}