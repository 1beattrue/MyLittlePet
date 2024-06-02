package edu.mirea.onebeattrue.mylittlepet.domain.pets.repository

import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun addNote(note: Note)
    suspend fun deleteNote(note: Note)

    fun getNoteList(petId: Int): Flow<List<Note>>
}