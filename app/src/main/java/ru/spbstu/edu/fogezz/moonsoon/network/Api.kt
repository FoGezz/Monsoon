package ru.spbstu.edu.fogezz.moonsoon.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("enter")
    suspend fun enter(@Query("nickname") nickname: String): Response<Void>

    @GET("list")
    suspend fun list(): Response<List<User>>
}