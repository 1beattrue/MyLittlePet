package edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.petinfo

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.AddPetUseCase
import edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.petinfo.PetInfoStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.petinfo.PetInfoStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.main.feed.petinfo.PetInfoStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface PetInfoStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class AddPet(val pet: Pet) : Intent
        data object ClickBack : Intent
    }

    data class State(
        val petWasAlreadyAdded: Boolean
    )

    sealed interface Label {
        data object ClickBack : Label
    }
}

class PetInfoStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val addPetUseCase: AddPetUseCase
) {

    fun create(): PetInfoStore =
        object : PetInfoStore, Store<Intent, State, Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = State(
                petWasAlreadyAdded = false
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {
        data object AddPet : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.AddPet -> {
                    scope.launch {
                        addPetUseCase(intent.pet.copy(id = Pet.UNDEFINED_ID))
                        dispatch(Msg.AddPet)
                    }
                }
                Intent.ClickBack -> {
                    publish(Label.ClickBack)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                Msg.AddPet -> copy(petWasAlreadyAdded = true)
            }
    }

    companion object {
        private val STORE_NAME = "PetInfoStore"
    }
}
