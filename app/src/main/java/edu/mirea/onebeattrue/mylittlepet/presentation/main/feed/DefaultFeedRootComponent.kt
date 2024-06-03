package edu.mirea.onebeattrue.mylittlepet.presentation.main.feed

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.general.DefaultFeedComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.petinfo.DefaultPetInfoComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.qrcode.DefaultQrCodeComponent
import kotlinx.serialization.Serializable

class DefaultFeedRootComponent @AssistedInject constructor(
    private val feedComponentFactory: DefaultFeedComponent.Factory,
    private val qrCodeComponentFactory: DefaultQrCodeComponent.Factory,
    private val petInfoComponentFactory: DefaultPetInfoComponent.Factory,

    @Assisted("onChangedBottomMenuVisibility") private val onChangedBottomMenuVisibility: (Boolean) -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : FeedRootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    private val initialConfig = Config.Feed

    override val stack: Value<ChildStack<*, FeedRootComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = initialConfig,
        handleBackButton = true,
        childFactory = ::child,
        key = "feed"
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
    ): FeedRootComponent.Child = when (config) {
        Config.Feed -> {
            val component = feedComponentFactory.create(
                onOpenPetInfoClicked = {
                    navigation.pushNew(
                        Config.PetInfo(
                            petString = "",
                            lastPet = it
                        )
                    )
                },
                onScanQrCodeClicked = { navigation.pushNew(Config.QrCode) },
                componentContext = componentContext
            )
            FeedRootComponent.Child.Feed(component)
        }

        Config.QrCode -> {
            val component = qrCodeComponentFactory.create(
                onBackClicked = {
                    navigation.pop()
                },
                onQrCodeScanned = { petString ->
                    navigation.replaceCurrent(Config.PetInfo(petString = petString))
                },
                componentContext = componentContext
            )
            FeedRootComponent.Child.QrCode(component)
        }

        is Config.PetInfo -> {
            val component = petInfoComponentFactory.create(
                onBackClicked = {
                    navigation.pop()
                },
                petString = config.petString,
                lastPet = config.lastPet,
                componentContext = componentContext
            )
            FeedRootComponent.Child.PetInfo(component)
        }
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object Feed : Config

        @Serializable
        data object QrCode : Config

        @Serializable
        data class PetInfo(
            val petString: String,
            val lastPet: Pet? = null
        ) : Config
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onChangedBottomMenuVisibility") onChangedBottomMenuVisibility: (Boolean) -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultFeedRootComponent
    }
}