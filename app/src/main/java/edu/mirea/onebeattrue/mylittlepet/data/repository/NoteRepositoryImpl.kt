package edu.mirea.onebeattrue.mylittlepet.data.repository

import edu.mirea.onebeattrue.mylittlepet.data.local.db.PetListDao
import edu.mirea.onebeattrue.mylittlepet.data.mapper.mapDbModelListToEntities
import edu.mirea.onebeattrue.mylittlepet.data.mapper.mapEntityToDbModel
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Note
import edu.mirea.onebeattrue.mylittlepet.domain.pets.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val petListDao: PetListDao,
) : NoteRepository {
    override suspend fun addNote(note: Note) {
        petListDao.addNote(note.mapEntityToDbModel())
    }

    override suspend fun deleteNote(note: Note) {
        petListDao.deleteNote(
            petId = note.petId,
            noteId = note.id
        )
    }

    override fun getNoteList(petId: Int): Flow<List<Note>> =
        petListDao.getNoteList(petId).map { listDbModel ->
            listDbModel.mapDbModelListToEntities()
        }

}