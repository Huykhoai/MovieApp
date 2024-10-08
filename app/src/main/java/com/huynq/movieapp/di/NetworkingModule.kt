package com.huynq.movieapp.di

import com.huynq.movieapp.retrofit.ApiClient
import com.huynq.movieapp.retrofit.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkingModule {
    @Provides
    @Singleton
    @Named("apiClient")
    fun provideApiClient(): Retrofit {
        return ApiClient.retrofit
    }

    @Provides
    @Singleton
    fun provideApiService(@Named("apiClient")retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}