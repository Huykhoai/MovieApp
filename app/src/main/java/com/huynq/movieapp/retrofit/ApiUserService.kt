package com.huynq.movieapp.retrofit

import com.huynq.movieapp.model.Movies
import com.huynq.movieapp.model.UserResponse
import com.huynq.movieapp.model.WatchListResponse
import com.huynq.movieapp.request.LoginRequest
import com.huynq.movieapp.utils.APIConstants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiUserService {
    @POST(APIConstants.LOGIN)
    suspend fun login(@Body login: LoginRequest): Response<UserResponse>
    @GET(APIConstants.get_user)
    suspend fun getUser(@Path("id") id: String): Response<UserResponse>
    @POST(APIConstants.add_watch_list)
    suspend fun addWatchList(@Body movie: Movies): Response<WatchListResponse>
    @GET(APIConstants.watch_list)
    suspend fun watchList(): Response<WatchListResponse>
}