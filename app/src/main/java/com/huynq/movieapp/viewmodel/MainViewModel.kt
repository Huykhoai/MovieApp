package com.huynq.movieapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huynq.movieapp.data.MovieResponsitory
import com.huynq.movieapp.model.MovieDetails
import com.huynq.movieapp.model.MovieResponse
import com.huynq.movieapp.model.ResponseCast
import com.huynq.movieapp.model.SearchResponse
import com.huynq.movieapp.model.VideoResponse
import javax.inject.Inject

class MainViewModel @Inject constructor(private val responsitory: MovieResponsitory) : ViewModel() {
    private val _popularMoviesLiveData = MutableLiveData<MovieResponse>()
    val popularMoviesLiveData : LiveData<MovieResponse> get() =_popularMoviesLiveData

    private val _upcommingMoviesLiveData = MutableLiveData<MovieResponse>()
    val upcommingMoviesLiveData : LiveData<MovieResponse> get() =_upcommingMoviesLiveData

    private val _detailMoviesLiveData = MutableLiveData<MovieDetails>()
    val detailMoviesLiveData : LiveData<MovieDetails> get() =_detailMoviesLiveData

    private val _videoMoviesLiveData = MutableLiveData<VideoResponse>()
    val videoMoviesLiveData : LiveData<VideoResponse> get() =_videoMoviesLiveData

    private val _castMoviesLiveData = MutableLiveData<ResponseCast>()
    val castMoviesLiveData : LiveData<ResponseCast> get() =_castMoviesLiveData

    private val _searchMoviesLiveData = MutableLiveData<SearchResponse>()
    val searchMoviesLiveData : LiveData<SearchResponse> get() =_searchMoviesLiveData

    suspend fun getPopularMovies(api_key: String) {
        val response = responsitory.getPopularMovies(api_key)
        if(response.isSuccessful){
            response.body()?.let {
                _popularMoviesLiveData.postValue(it)
                Log.i("Retrofit", "getPopularMovies: ${it.results} ")
            }
        }else{
            Log.i("Retrofit", "getPopularMovies Error: ${response.errorBody()} ")
        }
    }
    suspend fun getUpCommingMovies(api_key: String){
        val response = responsitory.getUpCommingMovies(api_key)
        if(response.isSuccessful){
            response.body()?.let {
                _upcommingMoviesLiveData.postValue(it)
                Log.i("Retrofit", "getUpCommingMovies: ${it.results} ")

            }
        }else{
            Log.i("Retrofit", "getUpCommingMovies: ${response.errorBody()}")
        }
    }
    suspend fun getDetailMovies(movie_id: Int,api_key: String) {
        val response = responsitory.getDetailMovies(movie_id, api_key)
        if (response.isSuccessful) {
            response.body()?.let {
                _detailMoviesLiveData.postValue(it)
                Log.i("Retrofit", "getDetailMovies: ${it} ")
            }
        }else{
            Log.i("Retrofit", "getDetailMovies: ${response.errorBody()}")
        }
    }
    suspend fun getVideosMovies(movie_id: Int,api_key: String){
        val response = responsitory.getVideos(movie_id, api_key)
        if(response.isSuccessful){
            response.body()?.let {
                _videoMoviesLiveData.postValue(it)
                Log.i("Retrofit", "getVideosMovies: ${it} ")
            }
        }else{
            Log.i("Retrofit", "getVideosMovies: ${response.errorBody()}")
        }
    }
    suspend fun getCastsMovies(movie_id: Int,api_key: String){
        val response = responsitory.getCasts(movie_id, api_key)
        if(response.isSuccessful){
            response.body()?.let {
                _castMoviesLiveData.postValue(it)
                Log.i("Retrofit", "getCastsMovies: ${it} ")
            }
        }else{
            Log.i("Retrofit", "getCastsMovies: ${response.errorBody()}")
        }
    }
    suspend fun searchMovies(query: String, api_key: String){
        val response = responsitory.searchMovies(query,api_key)
        if(response.isSuccessful){
            response.body()?.let {
                _searchMoviesLiveData.postValue(it)
                Log.i("Retrofit", "searchMovies: ${it} ")
            }
        }else{
            Log.i("Retrofit", "Error : ${response.errorBody()}")
        }
    }
}