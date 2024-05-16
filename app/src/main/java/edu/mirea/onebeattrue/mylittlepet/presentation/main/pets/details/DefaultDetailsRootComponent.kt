package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details

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
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Event
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.DefaultAddEventComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.eventlist.DefaultEventListComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.general.DefaultDetailsComponent
import kotlinx.serialization.Serializable

class DefaultDetailsRootComponent @AssistedInject constructor(
    private val detailsComponentFactory: DefaultDetailsComponent.Factory,
    private val addEventComponentFactory: DefaultAddEventComponent.Factory,
    private val eventListComponentFactory: DefaultEventListComponent.Factory,

    @Assisted("onBackClick") private val onBackClick: () -> Unit,
    @Assisted("pet") private val pet: Pet,
    @Assisted("componentContext") componentContext: ComponentContext
) : DetailsRootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    private val initialConfig = Config.Details

    override val stack: Value<ChildStack<*, DetailsRootComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = initialConfig,
        handleBackButton = true,
        childFactory = ::child,
        key = "details_root"
    )

    override fun onBackClicked() {
        onBackClick()
    }

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): DetailsRootComponent.Child = when (config) {
        is Config.AddEvent -> {
            val component = addEventComponentFactory.create(
                eventList = config.eventList,
                pet = pet,
                onAddEventClosed = {
                    navigation.pop()
                },
                componentContext = componentContext
            )
            DetailsRootComponent.Child.AddEvent(component)
        }

        Config.Details -> {
            val component = detailsComponentFactory.create(
                pet = pet,
                onClickBack = {
                    onBackClick()
                },
                onClickOpenEventList = {
                    navigation.pushNew(Config.EventList)
                },
                componentContext = componentContext
            )
            DetailsRootComponent.Child.Details(component)
        }

        Config.EventList -> {
            val component = eventListComponentFactory.create(
                pet = pet,
                onAddEvent = { eventList ->
                    navigation.pushNew(Config.AddEvent(eventList))
                },
                onClickBack = {
                    navigation.pop()
                },
                componentContext = componentContext
            )
            DetailsRootComponent.Child.EventList(component)
        }
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object Details : Config

        @Serializable
        data class AddEvent(val eventList: List<Event>) : Config

        @Serializable
        data object EventList : Config
    }

    @AssistedFactory
    interface Factory {
        fun create(

            @Assisted("pet") pet: Pet,
            @Assisted("onBackClick") onBackClick: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultDetailsRootComponent
    }
}