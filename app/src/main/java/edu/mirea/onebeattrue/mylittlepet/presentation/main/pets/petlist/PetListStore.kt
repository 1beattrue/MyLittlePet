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
        data class OpenDetails(val pet: Pet) : Intent
    }

    data class State(
        val screenState: ScreenState
    ) {
        sealed interface ScreenState {
            data object Empty : ScreenState
            data object Loading : ScreenState
            data class Loaded(val petList: List<Pet>) : ScreenState
        }
    }

    sealed interface Label {
        data object AddPet : Label
        data class EditPet(val pet: Pet) : Label
        data class OpenDetails(val pet: Pet) : Label
    }
}

class PetListStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getPetListUseCase: GetPetListUseCase,
    private val deletePetUseCase: DeletePetUseCase
) {

    fun create(): PetListStore =
        object : PetListStore, Store<Intent, State, Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = State(
                screenState = State.ScreenState.Empty
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data object Loading : Action
        data class PetListLoaded(val petList: List<Pet>) : Action
    }

    private sealed interface Msg {
        data object Loading : Msg
        data object Empty : Msg
        data class PetListUpdated(val petList: List<Pet>) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                dispatch(Action.Loading)
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

                is Intent.OpenDetails -> {
                    publish(Label.OpenDetails(intent.pet))
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.PetListLoaded -> {
                    if (action.petList.isEmpty()) {
                        dispatch(Msg.Empty)
                    } else {
                        dispatch(Msg.PetListUpdated(petList = action.petList))
                    }
                }

                Action.Loading -> {
                    dispatch(Msg.Loading)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.PetListUpdated -> copy(screenState = State.ScreenState.Loaded(msg.petList))
                Msg.Loading -> copy(screenState = State.ScreenState.Loading)
                Msg.Empty -> copy(screenState = State.ScreenState.Empty)
            }
    }

    companion object {
        private const val STORE_NAME = "PetListStore"
    }
}
