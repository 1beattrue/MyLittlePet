package edu.mirea.onebeattrue.mylittlepet.data.network.api

import edu.mirea.onebeattrue.mylittlepet.data.network.dto.EventDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface EventApiService {
    @POST("events/create")
    suspend fun createEvent(@Body event: EventDto): Int

    @GET("events/{id}")
    suspend fun getEventById(@Path("id") id: Int): EventDto

    @PUT("events/{id}")
    suspend fun updateEvent(@Path("id") id: Int, @Body event: EventDto): EventDto

    @DELETE("events/{id}")
    suspend fun deleteEvent(@Path("id") id: Int)

    @GET("events/byPetId/{petId}")
    suspend fun getEventsByPetId(@Path("petId") petId: Int): List<EventDto>

    @DELETE("events/irrelevant/{petId}")
    suspend fun deleteIrrelevantEventsByPetId(@Path("petId") petId: Int): List<EventDto>
}
