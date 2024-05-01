package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.type

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

class DefaultTypeComponent @AssistedInject constructor(
    private val storeFactory: TypeStoreFactory,

    @Assisted("onNextClicked") private val onNextClicked: (PetType) -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : TypeComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }

    init {
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    is TypeStore.Label.Next -> onNextClicked(it.petType)
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<TypeStore.State>
        get() = store.stateFlow

    override fun setPetType(petType: PetType) {
        store.accept(TypeStore.Intent.SetPetType(petType))
    }

    override fun next() {
        store.accept(TypeStore.Intent.Next)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onNextClicked") onNextClicked: (PetType) -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultTypeComponent
    }
}