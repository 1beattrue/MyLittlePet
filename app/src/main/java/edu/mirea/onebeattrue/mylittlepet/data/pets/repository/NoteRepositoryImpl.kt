package edu.mirea.onebeattrue.mylittlepet.data.pets.repository

import edu.mirea.onebeattrue.mylittlepet.data.pets.PetListDao
import edu.mirea.onebeattrue.mylittlepet.data.pets.PetMapper
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Note
import edu.mirea.onebeattrue.mylittlepet.domain.pets.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val petListDao: PetListDao,
    private val mapper: PetMapper
) : NoteRepository {
    override suspend fun addNote(note: Note) {
        petListDao.addNote(mapper.mapNoteEntityToDbModel(note))
    }

    override suspend fun deleteNote(note: Note) {
        petListDao.deleteNote(
            petId = note.petId,
            noteId = note.id
        )
    }

    override fun getNoteList(petId: Int): Flow<List<Note>> =
        petListDao.getNoteList(petId).map { listDbModel ->
            listDbModel.map { mapper.mapNoteDbModelToEntity(it) }
        }

}