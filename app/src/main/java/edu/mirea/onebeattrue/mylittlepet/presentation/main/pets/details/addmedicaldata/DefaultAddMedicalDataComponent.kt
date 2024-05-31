package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addmedicaldata

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalDataType
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addmedicaldata.photo.DefaultMedicalPhotoComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addmedicaldata.text.DefaultMedicalTextComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addmedicaldata.type.DefaultMedicalTypeComponent
import kotlinx.serialization.Serializable

class DefaultAddMedicalDataComponent @AssistedInject constructor(
    private val typeComponentFactory: DefaultMedicalTypeComponent.Factory,
    private val textComponentFactory: DefaultMedicalTextComponent.Factory,
    private val photoComponentFactory: DefaultMedicalPhotoComponent.Factory,

    @Assisted("pet") private val pet: Pet,
    @Assisted("onAddMedicalDataClosed") private val onAddMedicalDataClosed: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : AddMedicalDataComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, AddMedicalDataComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Type,
        handleBackButton = true,
        childFactory = ::child,
        key = "add_medical_data"
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): AddMedicalDataComponent.Child = when (config) {
        Config.Type -> {
            val component = typeComponentFactory.create(
                onNextClicked = { type ->
                    navigation.pushNew(Config.Text(type))
                },
                componentContext = componentContext
            )
            AddMedicalDataComponent.Child.Type(component)
        }

        is Config.Text -> {
            val component = textComponentFactory.create(
                medicalDataType = config.medicalDataType,
                onNextClicked = { type, text ->
                    navigation.pushNew(Config.Photo(type, text))
                },
                componentContext = componentContext
            )
            AddMedicalDataComponent.Child.Text(component)
        }

        is Config.Photo -> {
            val component = photoComponentFactory.create(
                medicalDataType = config.medicalDataType,
                medicalDataText = config.medicalDataText,
                medicalDataList = pet.medicalDataList,
                pet = pet,
                onFinished = {
                    onAddMedicalDataClosed()
                },
                componentContext = componentContext
            )
            AddMedicalDataComponent.Child.Photo(component)
        }
    }

    override fun onBackClicked() {
        onAddMedicalDataClosed()
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object Type : Config

        @Serializable
        data class Text(val medicalDataType: MedicalDataType) : Config

        @Serializable
        data class Photo(
            val medicalDataType: MedicalDataType,
            val medicalDataText: String
        ) : Config
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("pet") pet: Pet,
            @Assisted("onAddMedicalDataClosed") onAddMedicalDataClosed: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultAddMedicalDataComponent
    }
}