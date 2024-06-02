package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addmedicaldata.photo

import android.net.Uri
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalData
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalDataType
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.presentation.utils.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultMedicalPhotoComponent @AssistedInject constructor(
    private val storeFactory: MedicalPhotoStoreFactory,

    @Assisted("medicalDataType") private val medicalDataType: MedicalDataType,
    @Assisted("medicalDataText") private val medicalDataText: String,
    @Assisted("pet") private val pet: Pet,
    @Assisted("onFinished") private val onFinished: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : MedicalPhotoComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        storeFactory.create(
            medicalDataType = medicalDataType,
            medicalDataText = medicalDataText,
            pet = pet
        )
    }

    init {
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    MedicalPhotoStore.Label.AddMedicalData -> onFinished()
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<MedicalPhotoStore.State>
        get() = store.stateFlow

    override fun setPhoto(imageUri: Uri) {
       store.accept(MedicalPhotoStore.Intent.SetPhoto(imageUri))
    }

    override fun finish() {
        store.accept(MedicalPhotoStore.Intent.AddMedicalData)
    }

    override fun deletePhoto() {
        store.accept(MedicalPhotoStore.Intent.DeletePhoto)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("medicalDataType") medicalDataType: MedicalDataType,
            @Assisted("medicalDataText") medicalDataText: String,
            @Assisted("pet") pet: Pet,
            @Assisted("onFinished") onFinished: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultMedicalPhotoComponent
    }
}