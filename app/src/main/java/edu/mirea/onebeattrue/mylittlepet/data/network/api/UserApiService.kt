package edu.mirea.onebeattrue.mylittlepet.data.network.api

import edu.mirea.onebeattrue.mylittlepet.data.network.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApiService {
    @POST("users/create")
    suspend fun createUser(@Body user: UserDto): UserDto

    @GET("users/{token}")
    suspend fun getUserByToken(@Path("token") token: String): UserDto

    @DELETE("users/{token}")
    suspend fun deleteUser(@Path("token") token: String)
}