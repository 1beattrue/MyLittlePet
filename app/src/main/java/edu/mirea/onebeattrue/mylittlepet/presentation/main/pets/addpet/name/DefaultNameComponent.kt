package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.name

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

class DefaultNameComponent @AssistedInject constructor(
    private val storeFactory: NameStoreFactory,

    @Assisted("petType") private val petType: PetType,
    @Assisted("onNextClicked") private val onNextClicked: (PetType, String) -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : NameComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }

    init {
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    is NameStore.Label.Next -> onNextClicked(petType, it.petName)
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<NameStore.State>
        get() = store.stateFlow

    override fun setPetName(petName: String) {
        store.accept(NameStore.Intent.SetPetName(petName))
    }

    override fun next() {
        store.accept(NameStore.Intent.Next)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("petType") petType: PetType,
            @Assisted("onNextClicked") onNextClicked: (PetType, String) -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultNameComponent
    }
}