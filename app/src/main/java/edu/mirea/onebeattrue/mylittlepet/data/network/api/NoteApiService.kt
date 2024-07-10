package edu.mirea.onebeattrue.mylittlepet.data.network.api

import edu.mirea.onebeattrue.mylittlepet.data.network.dto.NoteDto
import retrofit2.http.*

interface NoteApiService {
    @POST("notes/create")
    suspend fun createNote(@Body note: NoteDto): Int

    @GET("notes/{id}")
    suspend fun getNoteById(@Path("id") id: Int): NoteDto

    @PUT("notes/{id}")
    suspend fun updateNote(@Path("id") id: Int, @Body note: NoteDto): NoteDto

    @DELETE("notes/{id}")
    suspend fun deleteNote(@Path("id") id: Int)

    @GET("notes/byPetId/{petId}")
    suspend fun getNotesByPetId(@Path("petId") petId: Int): List<NoteDto>
}
