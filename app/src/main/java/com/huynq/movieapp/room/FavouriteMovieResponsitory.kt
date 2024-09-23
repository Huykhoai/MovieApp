package com.huynq.movieapp.room

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FavouriteMovieResponsitory @Inject constructor(private val dao: FavoriteDAO) {
    val favouriteMovies : Flow<List<FavouriteMovie>> = dao.getAllFavourite()

    suspend fun insert(favouriteMovie: FavouriteMovie){
        dao.insertFavorite(favouriteMovie)
    }
    suspend fun delete(favouriteMovie: FavouriteMovie){
        dao.deleteFavourite(favouriteMovie)
    }
    suspend fun deleteAll(){
        dao.deleteAllFavourite()
    }
    suspend fun getMovieById(movie_id: Int): FavouriteMovie?{
        return dao.getMovieById(movie_id)
    }
    suspend fun getMovieByName(movie_name: String): Flow<List<FavouriteMovie>> = flow{
        val response = dao.getMovieByName(movie_name)
        emit(response)
    }
}