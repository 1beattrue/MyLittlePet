package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.general

import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import kotlinx.coroutines.flow.StateFlow

interface DetailsComponent {
    val pet: Pet
    val model: StateFlow<DetailsStore.State>

    fun setAge(age: Long)
    fun setWeight()

    fun onChangeWeightClick()
    fun onChangeAgeClick()

    fun onWeightChanges(weight: String)

    fun onCloseWeightBottomSheetClick()
    fun onCloseAgeBottomSheetClick()

    fun onClickEventList()
    fun onClickNoteList()
    fun onClickMedicalDataList()

    fun showQrCode()
    fun hideQrCode()

    fun onBackClicked()
}