package edu.mirea.onebeattrue.mylittlepet.data.remote.api

import edu.mirea.onebeattrue.mylittlepet.data.remote.dto.PetDto
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("pets")
    suspend fun getPetList(): List<PetDto>

    @GET("pets/{id}")
    suspend fun getPetById(@Path("id") id: Int): PetDto

    @POST("pets")
    suspend fun createPet(petDto: PetDto): Pet

    @PUT("pets/{id}")
    suspend fun updatePet(@Path("id") id: Int, newPetDto: PetDto): Pet

    @DELETE("pets/{id}")
    suspend fun deletePet(@Path("id") id: Int): String



}