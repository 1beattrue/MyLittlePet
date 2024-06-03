package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addmedicaldata.photo

import android.app.Application
import android.net.Uri
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalData
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalDataType
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase.AddMedicalDataUseCase
import edu.mirea.onebeattrue.mylittlepet.extensions.ImageUtils.saveImageToInternalStorage
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addmedicaldata.photo.MedicalPhotoStore.Intent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addmedicaldata.photo.MedicalPhotoStore.Label
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addmedicaldata.photo.MedicalPhotoStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface MedicalPhotoStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class SetPhoto(val imageUri: Uri) : Intent
        data object AddMedicalData : Intent
        data object DeletePhoto : Intent
    }

    data class State(
        val imageUri: Uri
    )

    sealed interface Label {
        data object AddMedicalData : Label
    }
}

class MedicalPhotoStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val addMedicalDataUseCase: AddMedicalDataUseCase,
    private val application: Application,
) {

    fun create(
        medicalDataType: MedicalDataType,
        medicalDataText: String,
        pet: Pet
    ): MedicalPhotoStore =
        object : MedicalPhotoStore, Store<Intent, State, Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = State(
                imageUri = Uri.EMPTY
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = {
                ExecutorImpl(
                    type = medicalDataType,
                    text = medicalDataText,
                    pet = pet
                )
            },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {
        data class SetPhoto(val imageUri: Uri) : Msg
        data object DeletePhoto : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl(
        private val type: MedicalDataType,
        private val text: String,
        private val pet: Pet,
    ) : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.AddMedicalData -> {
                    scope.launch {
                        val imageUri = getState().imageUri
                        val localUri = saveImageToInternalStorage(application, imageUri)

                        val newMedicalData = MedicalData(
                            type = type,
                            imageUri = localUri.toString(),
                            text = text,
                            petId = pet.id
                        )

                        addMedicalDataUseCase(newMedicalData)
                        publish(Label.AddMedicalData)
                    }
                }

                Intent.DeletePhoto -> {
                    dispatch(Msg.DeletePhoto)
                }

                is Intent.SetPhoto -> {
                    dispatch(Msg.SetPhoto(intent.imageUri))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.SetPhoto -> copy(imageUri = msg.imageUri)
                Msg.DeletePhoto -> copy(imageUri = Uri.EMPTY)
            }
    }

    private fun generateMedicalDataId(list: List<MedicalData>): Int {
        if (list.isEmpty()) return 0
        var maxId = list[0].id
        list.forEach {
            if (it.id > maxId) maxId = it.id
        }
        return maxId + 1
    }

    companion object {
        private const val STORE_NAME = "MedicalPhotoStore"
    }
}
