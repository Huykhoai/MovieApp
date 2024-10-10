package com.huynq.movieapp.retrofit

import com.huynq.movieapp.utils.APIConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            val retrofit: Retrofit by lazy {
                Retrofit.Builder()
                    .baseUrl(APIConstants.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
        fun <T> createService (serviceClass: Class<T>): T {
            return retrofit.create(serviceClass)
        }



}