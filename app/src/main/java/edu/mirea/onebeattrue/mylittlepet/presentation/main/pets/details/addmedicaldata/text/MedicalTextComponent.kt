package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addmedicaldata.text

import kotlinx.coroutines.flow.StateFlow

interface MedicalTextComponent {
    val model: StateFlow<MedicalTextStore.State>

    fun setText(text: String)

    fun next()
}