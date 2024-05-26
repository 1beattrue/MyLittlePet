package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addmedicaldata.type

import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalDataType
import kotlinx.coroutines.flow.StateFlow

interface MedicalTypeComponent {
    val model: StateFlow<TypeStore.State>

    fun setType(medicalDataType: MedicalDataType)

    fun next()

    fun changeDropdownMenuExpanded(expanded: Boolean)
}