package edu.mirea.onebeattrue.mylittlepet.data.network.api

import edu.mirea.onebeattrue.mylittlepet.data.network.dto.MedicalDataDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MedicalDataApiService {
    @POST("medical/create")
    suspend fun createMedicalData(@Body medicalData: MedicalDataDto): MedicalDataDto

    @GET("medical/{id}")
    suspend fun getMedicalDataById(@Path("id") id: Int): MedicalDataDto

    @PUT("medical/{id}")
    suspend fun updateMedicalData(
        @Path("id") id: Int,
        @Body medicalData: MedicalDataDto
    ): MedicalDataDto

    @DELETE("medical/{id}")
    suspend fun deleteMedicalData(@Path("id") id: Int)

    @GET("medical/byPetId/{petId}")
    suspend fun getMedicalDataByPetId(@Path("petId") petId: Int): List<MedicalDataDto>
}
