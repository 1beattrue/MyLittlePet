package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.petlist

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.DeletePetUseCase
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.GetPetListUseCase
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.petlist.PetListStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.petlist.PetListStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.petlist.PetListStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface PetListStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object AddPet : Intent
        data class EditPet(val pet: Pet) : Intent
        data class DeletePet(val pet: Pet) : Intent
    }

    data class State(
        val petList: List<Pet>
    )

    sealed interface Label {
        data object AddPet : Label
        data class EditPet(val pet: Pet) : Label
    }
}

class PetListStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getPetListUseCase: GetPetListUseCase,
    private val deletePetUseCase: DeletePetUseCase
) {

    fun create(): PetListStore =
        object : PetListStore, Store<Intent, State, Label> by storeFactory.create(
            name = "PetListStore",
            initialState = State(
                petList = listOf()
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class PetListLoaded(val petList: List<Pet>) : Action
    }

    private sealed interface Msg {
        data class PetListUpdated(val petList: List<Pet>) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getPetListUseCase().collect { petList ->
                    dispatch(Action.PetListLoaded(petList = petList))
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.AddPet -> {
                    publish(Label.AddPet)
                }

                is Intent.DeletePet -> {
                    scope.launch {
                        deletePetUseCase(intent.pet)
                    }
                }

                is Intent.EditPet -> {
                    publish(Label.EditPet(intent.pet))
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.PetListLoaded -> {
                    dispatch(Msg.PetListUpdated(petList = action.petList))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.PetListUpdated -> copy(petList = msg.petList)
            }
    }
}
