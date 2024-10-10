package com.huynq.movieapp.di

import com.huynq.movieapp.retrofit.ApiService
import com.huynq.movieapp.retrofit.ApiUser
import com.huynq.movieapp.retrofit.ApiUserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {
    @Provides
    @Singleton
    @Named("apiUser")
    fun provideApiUser(): Retrofit {
        return ApiUser.retrofit
    }
    @Provides
    @Singleton
    fun provideUserService(@Named("apiUser") retrofit: Retrofit): ApiUserService {
        return retrofit.create(ApiUserService::class.java)
    }
}