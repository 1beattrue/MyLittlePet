package edu.mirea.onebeattrue.mylittlepet.data.remote.api

import edu.mirea.onebeattrue.mylittlepet.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {
    @GET("users")
    suspend fun getUserList(): List<UserDto>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): UserDto

    @POST("users")
    suspend fun createUser(@Body userDto: UserDto): UserDto

    @PUT("users/{id}")
    suspend fun updateUserById(@Path("id") id: Int, newUser: UserDto): UserDto

    @DELETE("users/{id}")
    suspend fun deleteUserById(@Path("id") id: Int): String
}