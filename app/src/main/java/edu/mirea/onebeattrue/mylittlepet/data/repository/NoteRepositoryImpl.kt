package edu.mirea.onebeattrue.mylittlepet.data.repository

import edu.mirea.onebeattrue.mylittlepet.data.local.db.NoteDao
import edu.mirea.onebeattrue.mylittlepet.data.mapper.mapDbModelListToEntities
import edu.mirea.onebeattrue.mylittlepet.data.mapper.mapDtoToDbModel
import edu.mirea.onebeattrue.mylittlepet.data.mapper.mapEntityToDbModel
import edu.mirea.onebeattrue.mylittlepet.data.mapper.mapEntityToDto
import edu.mirea.onebeattrue.mylittlepet.data.network.api.NoteApiService
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Note
import edu.mirea.onebeattrue.mylittlepet.domain.pets.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val noteApiService: NoteApiService
) : NoteRepository {
    override suspend fun addNote(note: Note) {
        val noteId = noteApiService.createNote(note.mapEntityToDto())

        noteDao.addNote(note.mapEntityToDbModel().copy(id = noteId))
    }

    override suspend fun deleteNote(note: Note) {
        noteApiService.deleteNote(note.id)

        noteDao.deleteNote(
            petId = note.petId,
            noteId = note.id
        )
    }

    override suspend fun synchronizeWithServer(petId: Int) {
        val noteDtoList = noteApiService.getNotesByPetId(petId)

        val oldNotes = noteDao.getNoteList(petId).first()
        oldNotes.forEach { oldNote ->
            noteDao.deleteNote(petId, oldNote.id)
        }

        noteDtoList.forEach { noteDto ->
            noteDao.addNote(noteDto.mapDtoToDbModel())
        }
    }

    override fun getNoteList(petId: Int): Flow<List<Note>> =
        noteDao.getNoteList(petId).map { listDbModel ->
            listDbModel.mapDbModelListToEntities()
        }

}