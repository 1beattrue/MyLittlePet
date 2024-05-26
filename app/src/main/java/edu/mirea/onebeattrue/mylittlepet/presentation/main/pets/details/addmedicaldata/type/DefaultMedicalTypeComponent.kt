package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addmedicaldata.type

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

class DefaultMedicalTypeComponent @AssistedInject constructor(
    private val storeFactory: TypeStoreFactory,

    @Assisted("onNextClicked") private val onNextClicked: (MedicalDataType) -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : MedicalTypeComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }

    init {
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    is TypeStore.Label.Next -> onNextClicked(it.medicalDataType)
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<TypeStore.State>
        get() = store.stateFlow

    override fun setType(medicalDataType: MedicalDataType) {
        store.accept(TypeStore.Intent.SetType(medicalDataType))
    }

    override fun next() {
        store.accept(TypeStore.Intent.Next)
    }

    override fun changeDropdownMenuExpanded(expanded: Boolean) {
        store.accept(TypeStore.Intent.ChangeDropdownMenuExpanded(expanded))
    }


    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onNextClicked") onNextClicked: (MedicalDataType) -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultMedicalTypeComponent
    }
}