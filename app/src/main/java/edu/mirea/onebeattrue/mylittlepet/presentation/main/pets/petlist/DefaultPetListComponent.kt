package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.petlist

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.presentation.extensions.componentScope
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.DetailsStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultPetListComponent @AssistedInject constructor(
    private val storeFactory: PetListStoreFactory,

    @Assisted("onAddPetClicked") private val onAddPetClicked: () -> Unit,
    @Assisted("onEditPetClicked") private val onEditPetClicked: (Pet) -> Unit,
    @Assisted("onOpenDetails") private val onOpenDetails: (Pet) -> Unit,

    @Assisted("componentContext") componentContext: ComponentContext
) : PetListComponent, ComponentContext by componentContext {

    val store = instanceKeeper.getStore { storeFactory.create() }

    init {
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    PetListStore.Label.AddPet -> {
                        onAddPetClicked()
                    }

                    is PetListStore.Label.EditPet -> {
                        onEditPetClicked(it.pet)
                    }

                    is PetListStore.Label.OpenDetails -> {
                        onOpenDetails(it.pet)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<PetListStore.State>
        get() = store.stateFlow

    override fun addPet() {
        store.accept(PetListStore.Intent.AddPet)
    }

    override fun editPet(pet: Pet) {
        store.accept(PetListStore.Intent.EditPet(pet))
    }

    override fun deletePet(pet: Pet) {
        store.accept(PetListStore.Intent.DeletePet(pet))
    }

    override fun openDetails(pet: Pet) {
        store.accept(PetListStore.Intent.OpenDetails(pet))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onAddPetClicked") onAddPetClicked: () -> Unit,
            @Assisted("onEditPetClicked") onEditPetClicked: (Pet) -> Unit,
            @Assisted("onOpenDetails") onOpenDetails: (Pet) -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultPetListComponent
    }
}