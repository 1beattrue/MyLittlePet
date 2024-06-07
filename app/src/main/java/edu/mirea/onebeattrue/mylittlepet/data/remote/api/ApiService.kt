package edu.mirea.onebeattrue.mylittlepet.data.remote.api

import edu.mirea.onebeattrue.mylittlepet.data.remote.dto.PetDto
import edu.mirea.onebeattrue.mylittlepet.data.remote.dto.UserDto
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Event
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.MedicalData
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Note
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    // PetController

    @GET("pets")
    suspend fun getPetList(): List<PetDto>

    @GET("pets/{id}")
    suspend fun getPetById(@Path("id") id: Int): PetDto

    @POST("pets")
    suspend fun createPet(@Body petDto: PetDto): Pet

    @PUT("pets/{id}")
    suspend fun updatePetById(@Path("id") id: Int, newPetDto: PetDto): Pet

    @DELETE("pets/{id}")
    suspend fun deletePetById(@Path("id") id: Int): String

    // NoteController

    @GET("notes")
    suspend fun getNoteList(): List<Note>

    @GET("notes/{id}")
    suspend fun getNoteById(@Path("id") id: Int): Note

    @POST("notes")
    suspend fun createNote(@Body note: Note): Note

    @PUT("notes/{id}")
    suspend fun updateNoteById(@Path("id") id: Int, newNote: Note): Note

    @DELETE("notes/{id}")
    suspend fun deleteNoteById(@Path("id") id: Int): String

    // MedicalDataController

    @GET("medical_data")
    suspend fun getMedicalDataList(): List<MedicalData>

    @GET("medical_data/{id}")
    suspend fun getMedicalDataById(@Path("id") id: Int): MedicalData

    @POST("medical_data")
    suspend fun createMedicalData(@Body medicalData: MedicalData): MedicalData

    @PUT("medical_data/{id}")
    suspend fun updateMedicalDataById(@Path("id") id: Int, newMedicalData: MedicalData): MedicalData

    @DELETE("medical_data/{id}")
    suspend fun deleteMedicalDataById(@Path("id") id: Int): String

    // EventController

    @GET("events")
    suspend fun getEventList(): List<Event>

    @GET("events/{id}")
    suspend fun getEventById(@Path("id") id: Int): Event

    @POST("events")
    suspend fun createEvent(@Body event: Event): Event

    @PUT("events/{id}")
    suspend fun updateEventById(@Path("id") id: Int, newEvent: Event): Event

    @DELETE("events/{id}")
    suspend fun deleteEventById(@Path("id") id: Int): String

    // UserController

    @GET("users")
    suspend fun getUserList(): List<UserDto>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): UserDto

    @POST("users")
    suspend fun createUser(@Body user: UserDto): Response<UserDto>

    @PUT("users/{id}")
    suspend fun updateUserById(@Path("id") id: Int, newUser: UserDto): UserDto

    @DELETE("users/{id}")
    suspend fun deleteUserById(@Path("id") id: Int): String

}