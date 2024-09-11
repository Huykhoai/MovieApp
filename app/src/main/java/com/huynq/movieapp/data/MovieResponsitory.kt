package com.huynq.movieapp.data

import com.huynq.movieapp.retrofit.ApiClient
import com.huynq.movieapp.retrofit.ApiService
import javax.inject.Inject

class MovieResponsitory @Inject constructor() {
   private val restrofitService = ApiClient.createService(ApiService::class.java)
   suspend fun getPopularMovies(api_key: String) = restrofitService.getPopularMovies(api_key)
   suspend fun getUpCommingMovies(api_key: String) = restrofitService.getUpCommingMovies(api_key)
}