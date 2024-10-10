package com.huynq.movieapp.retrofit

import com.huynq.movieapp.model.UserResponse
import com.huynq.movieapp.request.LoginRequest
import com.huynq.movieapp.utils.APIConstants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiUserService {
    @POST(APIConstants.LOGIN)
    suspend fun login(@Body login: LoginRequest): Response<UserResponse>
}