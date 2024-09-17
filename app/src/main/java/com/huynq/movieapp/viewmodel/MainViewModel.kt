package com.huynq.movieapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huynq.movieapp.data.MovieResponsitory
import com.huynq.movieapp.model.MovieDetails
import com.huynq.movieapp.model.MovieResponse
import com.huynq.movieapp.model.ResponseCast
import com.huynq.movieapp.model.SearchResponse
import com.huynq.movieapp.model.VideoResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(private val responsitory: MovieResponsitory) : ViewModel() {
     val popularMoviesLiveData = MutableLiveData<MovieResponse>()
     val upcommingMoviesLiveData = MutableLiveData<MovieResponse>()
     val detailMoviesLiveData = MutableLiveData<MovieDetails>()
     val videoMoviesLiveData = MutableLiveData<VideoResponse>()
     val castMoviesLiveData = MutableLiveData<ResponseCast>()
     val searchMoviesLiveData = MutableLiveData<SearchResponse>()

     fun getPopularMovies() {
        viewModelScope.launch {
            responsitory.getPopularMovies()
                .catch {e->
                    Log.i("Retrofit", "getPopularMovies: ${e.message}")
                }
                .collect {
                    popularMoviesLiveData.postValue(it)
                }
        }
    }
     fun getUpCommingMovies(){
        viewModelScope.launch {
            responsitory.getUpCommingMovies()
                .catch { e->
                    Log.i("Retrofit", "getUpCommingMovies: ${e.message}")
                }
                .collect{
                    upcommingMoviesLiveData.postValue(it)
                }
        }
    }
    fun getDetailMovies(movie_id: Int) {
        viewModelScope.launch {
            responsitory.getDetailMovies(movie_id)
                .catch { e->
                    Log.i("Retrofit", "getDetailMovies: ${e.message}")
                }
                .collect{
                    detailMoviesLiveData.postValue(it)
                }
        }
    }
     fun getVideosMovies(movie_id: Int){
        viewModelScope.launch {
            responsitory.getVideos(movie_id)
                .catch { e->
                    Log.i("Retrofit", "getVideosMovies: ${e.message}")
                }
                .collect{
                    videoMoviesLiveData.postValue(it)
                }
        }
    }
     fun getCastsMovies(movie_id: Int){
        viewModelScope.launch {
            responsitory.getCasts(movie_id)
                .catch { e->
                    Log.i("Retrofit", "getCastsMovies: ${e.message}")
                }
                .collect{
                    castMoviesLiveData.postValue(it)
                }
        }
    }
     fun searchMovies(query: String){
       viewModelScope.launch {
           responsitory.getSearchMovies(query)
               .catch { e->
                   Log.i("Retrofit", "searchMovies: ${e.message}")
               }
               .collect{
                   searchMoviesLiveData.postValue(it)
               }
       }
    }

}