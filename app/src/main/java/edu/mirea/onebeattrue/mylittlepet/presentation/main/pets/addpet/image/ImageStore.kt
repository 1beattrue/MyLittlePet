package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.image

import android.app.Application
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
import edu.mirea.onebeattrue.mylittlepet.extensions.ImageUtils.saveImageToInternalStorage
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.image.ImageStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.image.ImageStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.addpet.image.ImageStore.State
import edu.mirea.onebeattrue.mylittlepet.presentation.utils.ImageUtils
import kotlinx.coroutines.launch
import javax.inject.Inject

interface ImageStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class SetPetImage(val imageUri: Uri) : Intent
        data object AddPet : Intent
        data object DeletePetImage : Intent
    }

    data class State(
        val image: ByteArray?
    )

    sealed interface Label {
        data object AddPet : Label
    }
}

class ImageStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val addPetUseCase: AddPetUseCase,
    private val editPetUseCase: EditPetUseCase,
    private val application: Application,
) {

    fun create(
        petType: PetType,
        petName: String,
        pet: Pet?
    ): ImageStore =
        object : ImageStore, Store<Intent, State, Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = State(
                pet?.image
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = { ExecutorImpl(petType = petType, petName = petName, pet = pet) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {
        data class SetPetImage(val image: ByteArray?) : Msg
        data object DeletePetImage : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl(
        private val petType: PetType,
        private val petName: String,
        private val pet: Pet?
    ) : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.AddPet -> {
                    scope.launch {
                        val image = getState().image

                        if (pet == null) {
                            addPetUseCase(
                                Pet(
                                    type = petType,
                                    name = petName,
                                    image = image
                                )
                            )
                        } else {
                            editPetUseCase(
                                pet.copy(
                                    name = petName,
                                    image = image
                                )
                            )
                        }

                        publish(Label.AddPet)
                    }
                }

                is Intent.SetPetImage -> {
                    scope.launch {
                        val imageUri = intent.imageUri
                        val localUri = saveImageToInternalStorage(application, imageUri)

                        val image = ImageUtils.uriToByteArray(application, localUri)

                        dispatch(Msg.SetPetImage(image))
                    }
                }
                Intent.DeletePetImage -> dispatch(Msg.DeletePetImage)
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.SetPetImage -> copy(image = msg.image)
                Msg.DeletePetImage -> copy(image = null)
            }
    }

    companion object {
        private const val STORE_NAME = "ImageStore"
    }
}
