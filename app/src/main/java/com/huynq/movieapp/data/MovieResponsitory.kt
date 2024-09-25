package com.huynq.movieapp.data

import com.huynq.movieapp.model.MovieDetails
import com.huynq.movieapp.model.MovieResponse
import com.huynq.movieapp.model.ResponseCast
import com.huynq.movieapp.model.SearchResponse
import com.huynq.movieapp.model.VideoResponse
import com.huynq.movieapp.retrofit.ApiClient
import com.huynq.movieapp.retrofit.ApiService
import com.huynq.movieapp.utils.APIConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MovieResponsitory @Inject constructor() {
   val apiService = ApiClient.createService(ApiService::class.java)

   fun getUpCommingMovies(): Flow<MovieResponse> = flow {
      val response = apiService.getUpCommingMovies(APIConstants.API_KEY, "1")
      emit(response.body()!!)
   }.flowOn(Dispatchers.IO)
   fun getPopularMovies(): Flow<MovieResponse> = flow {
      val response = apiService.getPopularMovies(APIConstants.API_KEY, "1")
      emit(response.body()!!)
   }.flowOn(Dispatchers.IO)
   fun getToprateMovies(): Flow<MovieResponse> = flow {
      val response = apiService.getToprateMovie(APIConstants.API_KEY, "1")
      emit(response.body()!!)
   }.flowOn(Dispatchers.IO)
   fun getSearchMovies(query: String): Flow<SearchResponse> = flow {
      val response = apiService.searchMovies(query, APIConstants.API_KEY)
      emit(response.body()!!)
   }.flowOn(Dispatchers.IO)
   fun getDetailMovies(movie_id: Int): Flow<MovieDetails> = flow {
      val response = apiService.getDetailsMovies(movie_id, APIConstants.API_KEY)
      emit(response.body()!!)
   }.flowOn(Dispatchers.IO)
   fun getVideos(movie_id: Int): Flow<VideoResponse> = flow {
      val response = apiService.getVideoDetails(movie_id, APIConstants.API_KEY)
      emit(response.body()!!)
   }.flowOn(Dispatchers.IO)
   fun getCasts(movie_id: Int): Flow<ResponseCast> = flow {
      val response = apiService.getMovieCredits(movie_id, APIConstants.API_KEY)
      emit(response.body()!!)
   }.flowOn(Dispatchers.IO)
   suspend fun getNowPlaying(): Flow<MovieResponse> = flow {
      val response = apiService.getNowPlaying(APIConstants.API_KEY,"1")
      emit(response.body()!!)
   }.flowOn(Dispatchers.IO)
}