package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.image

import android.net.Uri
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.PetType
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.AddPetUseCase
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.EditPetUseCase
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.image.ImageStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.image.ImageStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.image.ImageStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface ImageStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class SetPetImage(val imageUri: Uri) : Intent
        data object AddPet : Intent
        data class EditPet(val pet: Pet) : Intent
        data object DeletePetImage : Intent
    }

    data class State(
        val imageUri: Uri
    )

    sealed interface Label {
        data object AddPet : Label
    }
}

class ImageStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val addPetUseCase: AddPetUseCase,
    private val editPetUseCase: EditPetUseCase,
) {

    fun create(
        petType: PetType,
        petName: String
    ): ImageStore =
        object : ImageStore, Store<Intent, State, Label> by storeFactory.create(
            name = "ImageStore",
            initialState = State(
                imageUri = Uri.EMPTY
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = { ExecutorImpl(petType = petType, petName = petName) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {
        data class SetPetImage(val imageUri: Uri) : Msg
        data object DeletePetImage : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl(
        private val petType: PetType,
        private val petName: String
    ) : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.AddPet -> {
                    scope.launch {
                        val imageUri = getState().imageUri
                        addPetUseCase(
                            Pet(
                                type = petType,
                                name = petName,
                                imageUri = imageUri
                            )
                        )
                        publish(Label.AddPet)
                    }
                }

                is Intent.EditPet -> {
                    scope.launch {
                        val imageUri = getState().imageUri
                        editPetUseCase(
                            pet = intent.pet.copy(
                                name = petName,
                                imageUri = imageUri
                            )
                        )
                    }
                }

                is Intent.SetPetImage -> dispatch(Msg.SetPetImage(intent.imageUri))
                Intent.DeletePetImage -> dispatch(Msg.DeletePetImage)
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.SetPetImage -> copy(imageUri = msg.imageUri)
                Msg.DeletePetImage -> copy(imageUri = Uri.EMPTY)
            }
    }
}
