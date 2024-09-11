package com.huynq.movieapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huynq.movieapp.data.MovieResponsitory
import com.huynq.movieapp.model.MovieResponse
import javax.inject.Inject

class MainViewModel @Inject constructor(private val responsitory: MovieResponsitory) : ViewModel() {
    private val _popularMoviesLiveData = MutableLiveData<MovieResponse>()
    val popularMoviesLiveData : LiveData<MovieResponse> get() =_popularMoviesLiveData
    private val _upcommingMoviesLiveData = MutableLiveData<MovieResponse>()
    val upcommingMoviesLiveData : LiveData<MovieResponse> get() =_upcommingMoviesLiveData
    var currentMovieId = MutableLiveData<Int>()

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
}