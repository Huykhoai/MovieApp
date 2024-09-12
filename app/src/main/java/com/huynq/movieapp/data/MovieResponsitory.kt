package com.huynq.movieapp.data

import com.huynq.movieapp.retrofit.ApiClient
import com.huynq.movieapp.retrofit.ApiService
import javax.inject.Inject

class MovieResponsitory @Inject constructor() {
   private val restrofitService = ApiClient.createService(ApiService::class.java)
   suspend fun getPopularMovies(api_key: String) = restrofitService.getPopularMovies(api_key)
   suspend fun getUpCommingMovies(api_key: String) = restrofitService.getUpCommingMovies(api_key)
   suspend fun getDetailMovies(movie_id: Int,api_key: String) = restrofitService.getDetailsMovies(movie_id,api_key)
   suspend fun getVideos(movie_id: Int, api_key: String) = restrofitService.getVideoDetails(movie_id, api_key)
   suspend fun getCasts(movie_id: Int, api_key: String) = restrofitService.getMovieCredits(movie_id, api_key)
   suspend fun searchMovies(query: String, api_key: String) = restrofitService.searchMovies(query,api_key)
}