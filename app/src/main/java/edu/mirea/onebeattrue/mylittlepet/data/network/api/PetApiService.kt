package edu.mirea.onebeattrue.mylittlepet.data.network.api

import edu.mirea.onebeattrue.mylittlepet.data.network.dto.PetDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PetApiService {
    @POST("pets/create")
    suspend fun createPet(@Body pet: PetDto): Int

    @GET("pets/{id}")
    suspend fun getPetById(@Path("id") id: Int): PetDto

    @PUT("pets/{id}")
    suspend fun updatePet(@Path("id") id: Int, @Body pet: PetDto)

    @DELETE("pets/{id}")
    suspend fun deletePet(@Path("id") id: Int)

    @GET("pets/byUserToken/{userToken}")
    suspend fun getPetsByUserToken(@Path("userToken") userToken: String): List<PetDto>

    @POST("pets/copy/{petId}")
    suspend fun copyPet(
        @Path("petId") petId: Int,
        @Query("newUserToken") newUserToken: String
    ): PetDto
}