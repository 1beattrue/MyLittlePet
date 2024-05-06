package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details

import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Event
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalData
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Note
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import kotlinx.coroutines.flow.StateFlow

interface DetailsComponent {
    val pet: Pet
    val model: StateFlow<DetailsStore.State>

    fun setAge(age: Long)
    fun setWeight()

    fun onChangeWeightClick()
    fun onWeightChages(weight: String)
    fun onEventChages(label: String)

    fun onAddEventClick()
    fun onAddNoteClick()
    fun onAddMedicalDataClick()

    fun onCloseBottomSheetClick()

    fun addEvent()
    fun addNote()
    fun addMedicalData()

    fun onBackClicked()

    fun openDatePickerDialog()
    fun closeDatePickerDialog()
}