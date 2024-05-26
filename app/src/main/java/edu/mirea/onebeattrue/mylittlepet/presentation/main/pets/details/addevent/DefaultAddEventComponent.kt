package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Event
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.date.DefaultEventDateComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.text.DefaultEventTextComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.time.DefaultEventTimeComponent
import kotlinx.serialization.Serializable

class DefaultAddEventComponent @AssistedInject constructor(
    private val textComponentFactory: DefaultEventTextComponent.Factory,
    private val timeComponentFactory: DefaultEventTimeComponent.Factory,
    private val dateComponentFactory: DefaultEventDateComponent.Factory,

    @Assisted("pet") private val pet: Pet,
    @Assisted("eventList") private val eventList: List<Event>,
    @Assisted("onAddEventClosed") private val onAddEventClosed: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : AddEventComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, AddEventComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Text,
        handleBackButton = true,
        childFactory = ::child,
        key = "add_event"
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): AddEventComponent.Child = when (config) {
        Config.Text -> {
            val component = textComponentFactory.create(
                onNextClicked = { eventText ->
                    navigation.pushNew(Config.Time(eventText))
                },
                componentContext = componentContext
            )
            AddEventComponent.Child.Text(component)
        }

        is Config.Time -> {
            val component = timeComponentFactory.create(
                eventList = eventList,
                eventText = config.eventText,
                pet = pet,
                onNextClicked = { h, m ->
                    navigation.pushNew(Config.Date(config.eventText, h, m))
                },
                onFinish = {
                    onAddEventClosed()
                },
                componentContext = componentContext
            )
            AddEventComponent.Child.Time(component)
        }

        is Config.Date -> {
            val component = dateComponentFactory.create(
                eventList = eventList,
                eventText = config.eventText,
                eventTimeHours = config.eventTimeHours,
                eventTimeMinutes = config.eventTimeMinutes,
                pet = pet,
                onFinish = {
                    onAddEventClosed()
                },
                componentContext = componentContext
            )
            AddEventComponent.Child.Date(component)
        }
    }

    override fun onBackClicked() {
        onAddEventClosed()
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object Text : Config

        @Serializable
        data class Time(val eventText: String) : Config

        @Serializable
        data class Date(
            val eventText: String,
            val eventTimeHours: Int,
            val eventTimeMinutes: Int,
        ) : Config
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("eventList") eventList: List<Event>,
            @Assisted("pet") pet: Pet,
            @Assisted("onAddEventClosed") onAddEventClosed: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultAddEventComponent
    }
}