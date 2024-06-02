package edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase

import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Note
import edu.mirea.onebeattrue.mylittlepet.domain.pets.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNoteListUseCase @Inject constructor(private val repository: NoteRepository) {
    operator fun invoke(petId: Int): Flow<List<Note>> {
        return repository.getNoteList(petId)
    }
}