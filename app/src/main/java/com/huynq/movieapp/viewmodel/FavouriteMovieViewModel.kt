package com.huynq.movieapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.huynq.movieapp.room.FavouriteMovie
import com.huynq.movieapp.room.FavouriteMovieResponsitory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavouriteMovieViewModel @Inject constructor(
    private val favResponsitory: FavouriteMovieResponsitory)
    : ViewModel() {

    val favouriteMovies : LiveData<List<FavouriteMovie>> = favResponsitory.favouriteMovies.asLiveData()
    var _searchResult = MutableLiveData<List<FavouriteMovie>>()
    val searchResult : LiveData<List<FavouriteMovie>> get() = _searchResult
    suspend fun insert(favouriteMovie: FavouriteMovie) : Int{
        return withContext(Dispatchers.IO) {
            try {
                val existsMovie = favResponsitory.getMovieById(favouriteMovie.movie_id)
                if (existsMovie != null) {
                    Log.i("RoomDB", "Movie with id ${favouriteMovie.movie_id} already exists in the database.")
                    return@withContext 1
                } else {
                    favResponsitory.insert(favouriteMovie)
                    return@withContext 0
                }
            } catch (e: Exception) {
                Log.e("RoomDB", "Failed to insert movie", e)
                return@withContext -1
            }
        }
    }
    fun delete(favouriteMovie: FavouriteMovie){
        viewModelScope.launch {
            favResponsitory.delete(favouriteMovie)
        }
    }
    fun deleteAll(){
        viewModelScope.launch {
            favResponsitory.deleteAll()
        }
    }
    fun getMovieById(movie_id: Int): LiveData<FavouriteMovie?>{
        var result = MutableLiveData<FavouriteMovie?>()
        viewModelScope.launch {
           var movie = favResponsitory.getMovieById(movie_id)
            result.postValue(movie)
        }
        return result
    }
    fun getMovieByName(movie_name: String){
        viewModelScope.launch {
            favResponsitory.getMovieByName(movie_name)
                .catch { e ->
                    Log.e("RoomDB", "Failed to get movies by name", e)
                }
                .collect { movies ->
                    _searchResult.postValue(movies)
                    Log.i("Retrofit", "Search results = $movies")
                }
        }
    }
}