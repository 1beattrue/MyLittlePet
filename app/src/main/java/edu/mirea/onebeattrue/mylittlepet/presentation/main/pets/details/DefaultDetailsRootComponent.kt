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
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalData
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.DefaultAddEventComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addmedicaldata.DefaultAddMedicalDataComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addnote.DefaultAddNoteComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.eventlist.DefaultEventListComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.general.DefaultDetailsComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.medicaldatadetails.DefaultMedicalDataDetailsComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.medicaldatalist.DefaultMedicalDataListComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.notelist.DefaultNoteListComponent
import kotlinx.serialization.Serializable

class DefaultDetailsRootComponent @AssistedInject constructor(
    private val detailsComponentFactory: DefaultDetailsComponent.Factory,
    private val addEventComponentFactory: DefaultAddEventComponent.Factory,
    private val eventListComponentFactory: DefaultEventListComponent.Factory,
    private val noteListComponentFactory: DefaultNoteListComponent.Factory,
    private val addNoteComponentFactory: DefaultAddNoteComponent.Factory,
    private val medicalDataListComponentFactory: DefaultMedicalDataListComponent.Factory,
    private val addMedicalDataComponentFactory: DefaultAddMedicalDataComponent.Factory,
    private val medicalDataDetailsComponentFactory: DefaultMedicalDataDetailsComponent.Factory,

    @Assisted("onBackClick") private val onBackClick: () -> Unit,
    @Assisted("lastPet") private val pet: Pet,
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
                pet = config.pet,
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
                onClickOpenNoteList = {
                    navigation.pushNew(Config.NoteList)
                },
                onClickOpenMedicalDataList = {
                    navigation.pushNew(Config.MedicalDataList)
                },
                componentContext = componentContext
            )
            DetailsRootComponent.Child.Details(component)
        }

        Config.EventList -> {
            val component = eventListComponentFactory.create(
                pet = pet,
                onAddEvent = {
                    navigation.pushNew(Config.AddEvent(it))
                },
                onClickBack = {
                    navigation.pop()
                },
                componentContext = componentContext
            )
            DetailsRootComponent.Child.EventList(component)
        }

        Config.NoteList -> {
            val component = noteListComponentFactory.create(
                pet = pet,
                onAddNoteClicked = {
                    navigation.pushNew(Config.AddNote(it))
                },
                onClickBack = {
                    navigation.pop()
                },
                componentContext = componentContext
            )
            DetailsRootComponent.Child.NoteList(component)
        }

        is Config.AddNote -> {
            val component = addNoteComponentFactory.create(
                pet = config.pet,
                onAddNoteClosed = {
                    navigation.pop()
                },
                componentContext = componentContext
            )
            DetailsRootComponent.Child.AddNote(component)
        }

        is Config.MedicalDataList -> {
            val component = medicalDataListComponentFactory.create(
                pet = pet,
                onAddMedicalDataClicked = {
                    navigation.pushNew(Config.AddMedicalData(it))
                },
                onClickBack = {
                    navigation.pop()
                },
                onPhotoOpened = { medicalData ->
                    navigation.pushNew(Config.MedicalDataDetails(medicalData))
                },
                componentContext = componentContext
            )
            DetailsRootComponent.Child.MedicalDataList(component)
        }

        is Config.AddMedicalData -> {
            val component = addMedicalDataComponentFactory.create(
                pet = config.pet,
                onAddMedicalDataClosed = {
                    navigation.pop()
                },
                componentContext = componentContext
            )
            DetailsRootComponent.Child.AddMedicalData(component)
        }

        is Config.MedicalDataDetails -> {
            val component = medicalDataDetailsComponentFactory.create(
                medicalData = config.medicalData,
                onBackClicked = {
                    navigation.pop()
                },
                componentContext = componentContext
            )
            DetailsRootComponent.Child.MedicalDataDetails(component)
        }
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object Details : Config

        @Serializable
        data object EventList : Config

        @Serializable
        data class AddEvent(val pet: Pet) : Config

        @Serializable
        data object NoteList : Config

        @Serializable
        data class AddNote(val pet: Pet) : Config

        @Serializable
        data object MedicalDataList : Config

        @Serializable
        data class AddMedicalData(val pet: Pet) : Config

        @Serializable
        data class MedicalDataDetails(val medicalData: MedicalData) : Config
    }

    @AssistedFactory
    interface Factory {
        fun create(

            @Assisted("lastPet") pet: Pet,
            @Assisted("onBackClick") onBackClick: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultDetailsRootComponent
    }
}