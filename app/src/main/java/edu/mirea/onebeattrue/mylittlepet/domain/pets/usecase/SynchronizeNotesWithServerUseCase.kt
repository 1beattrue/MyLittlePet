package edu.mirea.onebeattrue.mylittlepet.domain.pets.usecase

import edu.mirea.onebeattrue.mylittlepet.domain.pets.repository.NoteRepository
import javax.inject.Inject

class SynchronizeNotesWithServerUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(petId: Int) {
        repository.synchronizeWithServer(petId)
    }
}