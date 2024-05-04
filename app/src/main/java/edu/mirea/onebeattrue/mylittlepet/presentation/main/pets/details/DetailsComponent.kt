package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details

import android.widget.DatePicker
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Event
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalData
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Note
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import kotlinx.coroutines.flow.StateFlow

interface DetailsComponent {
    val pet: Pet
    val model: StateFlow<DetailsStore.State>

    fun setAge(age: DatePicker)
    fun setWeight(weight: Int)

    fun onAddEventClick()
    fun onAddNoteClick()
    fun onAddMedicalDataClick()

    fun onCloseBottomSheetClick()

    fun addEvent(event: Event)
    fun addNote(note: Note)
    fun addMedicalData(medicalData: MedicalData)
}