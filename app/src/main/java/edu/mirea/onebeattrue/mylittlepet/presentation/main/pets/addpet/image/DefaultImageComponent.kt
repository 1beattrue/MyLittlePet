package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.image

import android.net.Uri
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.PetType
import edu.mirea.onebeattrue.mylittlepet.presentation.utils.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultImageComponent @AssistedInject constructor(
    private val storeFactory: ImageStoreFactory,

    @Assisted("petType") override val petType: PetType,
    @Assisted("petName") private val petName: String,
    @Assisted("lastPet") private val pet: Pet?,
    @Assisted("onFinished") private val onFinished: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : ImageComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create(petType, petName, pet) }

    init {
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    ImageStore.Label.AddPet -> onFinished()
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<ImageStore.State>
        get() = store.stateFlow

    override fun setPetImage(imageUri: Uri) {
        store.accept(ImageStore.Intent.SetPetImage(imageUri))
    }

    override fun finish() {
        store.accept(ImageStore.Intent.AddPet)
    }

    override fun deletePetImage() {
        store.accept(ImageStore.Intent.DeletePetImage)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("petType") petType: PetType,
            @Assisted("petName") petName: String,
            @Assisted("lastPet") pet: Pet? = null,
            @Assisted("onFinished") onFinished: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultImageComponent
    }
}