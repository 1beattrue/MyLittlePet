package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.general

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Event
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addevent.AddEventComponent
import edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.eventlist.EventListComponent
import kotlinx.coroutines.flow.StateFlow

interface DetailsComponent {
    val pet: Pet
    val model: StateFlow<DetailsStore.State>

    fun setAge(age: Long)
    fun setWeight()
    fun onChangeWeightClick()
    fun onWeightChanges(weight: String)
    fun onCloseBottomSheetClick()
    fun onBackClicked()
    fun openDatePickerDialog()
    fun closeDatePickerDialog()

    fun onClickEventList()
}