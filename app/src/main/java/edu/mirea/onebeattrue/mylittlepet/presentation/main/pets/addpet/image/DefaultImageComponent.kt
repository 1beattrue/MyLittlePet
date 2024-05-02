package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.image

import android.net.Uri
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.PetType
import edu.mirea.onebeattrue.mylittlepet.presentation.extensions.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultImageComponent @AssistedInject constructor(
    private val storeFactory: ImageStoreFactory,

    @Assisted("petType") override val petType: PetType,
    @Assisted("petName") private val petName: String,
    @Assisted("onAddPetClosed") private val onAddPetClosed: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : ImageComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create(petType, petName) }

    init {
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    ImageStore.Label.AddPet -> onAddPetClosed()
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

    override fun addPet() {
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
            @Assisted("onAddPetClosed") onAddPetClosed: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultImageComponent
    }
}