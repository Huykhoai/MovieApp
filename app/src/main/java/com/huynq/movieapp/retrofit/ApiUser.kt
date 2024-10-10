package com.huynq.movieapp.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiUser {
    private val BASE_URL = "http://192.168.124.12:3000"
    val loggingInterceptor = HttpLoggingInterceptor().apply {//log chi tieets các yc và phản hồi
        level = HttpLoggingInterceptor.Level.BODY// leval body
    }
    val client = OkHttpClient.Builder()// built client to network request
        .addInterceptor(loggingInterceptor)// write log network requests
        .build()
    val retrofit : Retrofit by lazy {// built retrofit
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())//convert from json reponse to object kotlin
            .build()
    }
    fun <T> createService (serviceClass: Class<T>): T{
        return retrofit.create(serviceClass)
    }
}