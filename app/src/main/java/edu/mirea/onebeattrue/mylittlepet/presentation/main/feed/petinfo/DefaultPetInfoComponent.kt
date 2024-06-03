package edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.petinfo

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.presentation.utils.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultPetInfoComponent @AssistedInject constructor(
    private val storeFactory: PetInfoStoreFactory,

    @Assisted("onBackClicked") private val onBackClicked: () -> Unit,
    @Assisted("petString") private val petString: String,
    @Assisted("componentContext") componentContext: ComponentContext
) : PetInfoComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }

    init {
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    PetInfoStore.Label.ClickBack -> onBackClicked()
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<PetInfoStore.State>
        get() = store.stateFlow

    override val pet: Pet? = try {
        Gson().fromJson(petString, Pet::class.java)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    override fun onClickBack() {
        store.accept(PetInfoStore.Intent.ClickBack)
    }

    override fun addPet() {
        pet?.let {
            store.accept(PetInfoStore.Intent.AddPet(it))
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onBackClicked") onBackClicked: () -> Unit,
            @Assisted("petString") petString: String,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultPetInfoComponent
    }
}