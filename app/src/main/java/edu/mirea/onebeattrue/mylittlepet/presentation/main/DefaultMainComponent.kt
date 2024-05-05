package edu.mirea.onebeattrue.mylittlepet.presentation.main

import android.util.Log
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.popWhile
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
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
import kotlinx.serialization.Serializable

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

    private val navigation = StackNavigation<Config>()

    private val initialConfig = Config.Pets

    override val stack: Value<ChildStack<*, MainComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = initialConfig,
        handleBackButton = false,
        childFactory = ::child,
        key = "main"
    )

    private val backCallback = BackCallback {
        navigation.popWhile { topOfStack: Config -> topOfStack != initialConfig }
    }

    init {
        registerBackHandler()
    }

    private fun registerBackHandler() {
        backHandler.register(backCallback)

        stack.subscribe {
            val isFirstTab = it.active.configuration == initialConfig
            backCallback.isEnabled = !isFirstTab
        }
    }

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): MainComponent.Child = when (config) {
        Config.Feed -> {
            val component = feedComponentFactory.create(
                componentContext = componentContext,
                onChangedBottomMenuVisibility = {
                    changeBottomMenuVisibility(it)
                }
            )
            MainComponent.Child.Feed(component)
        }

        Config.Pets -> {
            val component = petsComponentFactory.create(
                componentContext = componentContext,
                onChangedBottomMenuVisibility = {
                    changeBottomMenuVisibility(it)
                }
            )
            MainComponent.Child.Pets(component)
        }

        Config.Profile -> {
            val component = profileComponentFactory.create(
                componentContext = componentContext,
                onSignOutClicked = { onSignOutClicked() },
                onChangedBottomMenuVisibility = {
                    changeBottomMenuVisibility(it)
                }
            )
            MainComponent.Child.Profile(component)
        }
    }

    override fun navigateTo(navigationItem: NavigationItem) {
        when (navigationItem) {
            NavigationItem.FeedItem -> navigation.bringToFront(Config.Feed)
            NavigationItem.PetsItem -> navigation.bringToFront(Config.Pets)
            NavigationItem.ProfileItem -> navigation.bringToFront(Config.Profile)
        }
    }

    override fun changeBottomMenuVisibility(visibility: Boolean) {
        store.accept(MainStore.Intent.ChangeBottomMenuVisibility(visibility))
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object Feed : Config

        @Serializable
        data object Pets : Config

        @Serializable
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