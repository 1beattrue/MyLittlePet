package edu.mirea.onebeattrue.mylittlepet.presentation.main.pets.details.notelist

import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Note
import kotlinx.coroutines.flow.StateFlow

interface NoteListComponent {
    val model: StateFlow<NoteListStore.State>

    fun onAddNote()

    fun onDeleteNote(note: Note)

    fun onBackClicked()
}