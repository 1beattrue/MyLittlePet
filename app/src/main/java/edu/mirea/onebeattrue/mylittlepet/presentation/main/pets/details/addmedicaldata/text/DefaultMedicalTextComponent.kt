package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addmedicaldata.text

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalDataType
import edu.mirea.onebeattrue.mylittlepet.presentation.extensions.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultMedicalTextComponent @AssistedInject constructor(
    private val storeFactory: MedicalTextStoreFactory,

    @Assisted("medicalDataType") private val medicalDataType: MedicalDataType,
    @Assisted("onNextClicked") private val onNextClicked: (MedicalDataType, String) -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : MedicalTextComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }

    init {
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    is MedicalTextStore.Label.Next -> onNextClicked(medicalDataType, it.text)
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<MedicalTextStore.State>
        get() = store.stateFlow

    override fun setText(text: String) {
        store.accept(MedicalTextStore.Intent.SetText(text))
    }

    override fun next() {
        store.accept(MedicalTextStore.Intent.Next)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("medicalDataType") medicalDataType: MedicalDataType,
            @Assisted("onNextClicked") onNextClicked: (MedicalDataType, String) -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultMedicalTextComponent
    }
}