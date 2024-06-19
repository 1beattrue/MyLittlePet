package edu.mirea.onebeattrue.mylittlepet.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import edu.mirea.onebeattrue.mylittlepet.data.local.model.NoteDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(noteDbModel: NoteDbModel)

    @Query("DELETE FROM notes WHERE petId = :petId AND id = :noteId")
    suspend fun deleteNote(petId: Int, noteId: Int)

    @Query("SELECT * FROM notes WHERE petId = :petId")
    fun getNoteList(petId: Int): Flow<List<NoteDbModel>>
}