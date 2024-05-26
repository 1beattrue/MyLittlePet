package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.addnote

import kotlinx.coroutines.flow.StateFlow

interface AddNoteComponent {
    val model: StateFlow<AddNoteStore.State>

    fun iconChanged(icon: NoteIcon)
    fun noteTextChanged(text: String)
    fun addNote()
    fun onBackClicked()
}