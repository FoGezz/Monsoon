package ru.spbstu.edu.fogezz.moonsoon.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Network {
    private const val BASE_URL = "http://10.0.2.2:8000/"
    private val gson = GsonBuilder()
        .setLenient()
        .create()

    //private val moshi = Moshi.Builder()
//    .add(KotlinJsonAdapterFactory())
//    .build()


    private val retrofit = Retrofit.Builder()
        .client(
            OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                )
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create(gson))
//    .addConverterFactory(moshi)
        .baseUrl(BASE_URL)
        .build()


    val api: Api by lazy { retrofit.create(Api::class.java) }
}