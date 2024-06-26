package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.petlist

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.mylittlepet.domain.auth.usecase.SynchronizeUserWithServerUseCase
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.DeletePetUseCase
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.GetPetListUseCase
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.SynchronizePetsWithServerUseCase
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.petlist.PetListStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.petlist.PetListStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.petlist.PetListStore.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface PetListStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object AddPet : Intent
        data class EditPet(val pet: Pet) : Intent
        data class DeletePet(val pet: Pet) : Intent
        data class OpenDetails(val pet: Pet) : Intent
        data object Synchronize : Intent
    }

    data class State(
        val isLoading: Boolean,
        val syncError: Boolean,
        val deletePetErrorId: Int?,
        val petList: PetListState,
        val nowDeletingId: Int?
    ) {
        sealed interface PetListState {
            data object Initial : PetListState
            data class HasData(val pets: List<Pet>) : PetListState
            data object Empty : PetListState
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
    private val deletePetUseCase: DeletePetUseCase,
    private val synchronizeUserWithServerUseCase: SynchronizeUserWithServerUseCase,
    private val synchronizePetsWithServerUseCase: SynchronizePetsWithServerUseCase
) {

    fun create(): PetListStore =
        object : PetListStore, Store<Intent, State, Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = State(
                isLoading = true,
                syncError = false,
                deletePetErrorId = null,
                petList = State.PetListState.Initial,
                nowDeletingId = null
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data object Loading : Action
        data class PetListLoaded(val petList: List<Pet>) : Action
        data class SyncResult(val isError: Boolean) : Action
    }

    private sealed interface Msg {
        data object Loading : Msg
        data class PetListUpdated(val petList: List<Pet>) : Msg
        data class SyncResult(val isError: Boolean) : Msg
        data class DeletePetError(val petId: Int) : Msg
        data class DeletingPet(val id: Int) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                dispatch(Action.Loading)
                try {
                    synchronize()
                    dispatch(Action.SyncResult(isError = false))
                } catch (_: Exception) {
                    dispatch(Action.SyncResult(isError = true))
                } finally {
                    getPetListUseCase().collect { petList ->
                        dispatch(
                            Action.PetListLoaded(
                                petList = petList
                            )
                        )
                    }
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
                        try {
                            dispatch(Msg.DeletingPet(intent.pet.id))
                            withContext(Dispatchers.IO) { deletePetUseCase(intent.pet) }
                        } catch (_: Exception) {
                            delay(500)
                            dispatch(Msg.DeletePetError(intent.pet.id))
                        }
                    }
                }

                is Intent.EditPet -> {
                    publish(Label.EditPet(intent.pet))
                }

                is Intent.OpenDetails -> {
                    publish(Label.OpenDetails(intent.pet))
                }

                Intent.Synchronize -> {
                    scope.launch {
                        dispatch(Msg.Loading)
                        try {
                            synchronize()
                            dispatch(Msg.SyncResult(isError = false))
                        } catch (_: Exception) {
                            dispatch(Msg.SyncResult(isError = true))
                        }
                    }
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.PetListLoaded -> {
                    dispatch(Msg.PetListUpdated(petList = action.petList))
                }

                Action.Loading -> {
                    dispatch(Msg.Loading)
                }

                is Action.SyncResult -> {
                    dispatch(Msg.SyncResult(action.isError))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.PetListUpdated -> copy(
                    isLoading = false,
                    petList = if (msg.petList.isEmpty()) {
                        State.PetListState.Empty
                    } else {
                        State.PetListState.HasData(
                            msg.petList
                        )
                    },
                    deletePetErrorId = null,
                )

                Msg.Loading -> copy(
                    isLoading = true,
                    deletePetErrorId = null
                )

                is Msg.SyncResult -> copy(
                    syncError = msg.isError,
                    isLoading = false,
                    deletePetErrorId = null
                )

                is Msg.DeletePetError -> copy(
                    deletePetErrorId = msg.petId,
                    nowDeletingId = null
                )

                is Msg.DeletingPet -> copy(
                    nowDeletingId = msg.id,
                    deletePetErrorId = null
                )
            }
    }

    private suspend fun synchronize() {
        withContext(Dispatchers.IO) {
            synchronizeUserWithServerUseCase()
            synchronizePetsWithServerUseCase()
        }
    }

    companion object {
        private const val STORE_NAME = "PetListStore"
    }
}
