package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.general

import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Event
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import kotlinx.coroutines.flow.StateFlow

interface DetailsComponent {
    val pet: Pet
    val model: StateFlow<DetailsStore.State>

    fun setAge(age: Long)
    fun setWeight()

    fun onChangeWeightClick()
    fun onWeightChanges(weight: String)

    fun onAddEventClick()
    fun onAddNoteClick()
    fun onAddMedicalDataClick()

    fun onDeleteEvent(event: Event)

    fun onCloseBottomSheetClick()


    fun addNote()
    fun addMedicalData()

    fun onBackClicked()

    fun openDatePickerDialog()
    fun closeDatePickerDialog()
}