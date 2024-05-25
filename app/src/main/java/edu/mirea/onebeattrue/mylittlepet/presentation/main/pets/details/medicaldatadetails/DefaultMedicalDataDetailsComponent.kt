package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.medicaldatadetails

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalData

class DefaultMedicalDataDetailsComponent @AssistedInject constructor(
    @Assisted("medicalData") override val medicalData: MedicalData,
    @Assisted("onBackClicked") private val onBackClicked: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : MedicalDataDetailsComponent, ComponentContext by componentContext {

    override fun downloadPhoto() {
        TODO("Not yet implemented")
    }

    override fun onBackClick() {
        onBackClicked()
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("medicalData") medicalData: MedicalData,
            @Assisted("onBackClicked") onBackClicked: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultMedicalDataDetailsComponent
    }
}