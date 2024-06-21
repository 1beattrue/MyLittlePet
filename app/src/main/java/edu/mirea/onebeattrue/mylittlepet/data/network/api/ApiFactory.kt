package edu.mirea.onebeattrue.mylittlepet.data.network.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiFactory {
    private const val BASE_URL = "https://mylittlepetserver.onrender.com/api/v1/"

    private val interceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
//        .connectTimeout(30, TimeUnit.SECONDS)
//        .writeTimeout(30, TimeUnit.SECONDS)
//        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val userApiService: UserApiService = retrofit.create()
    val petApiService: PetApiService = retrofit.create()
    val eventApiService: EventApiService = retrofit.create()
    val noteApiService: NoteApiService = retrofit.create()
    val medicalDataApiService: MedicalDataApiService = retrofit.create()
}