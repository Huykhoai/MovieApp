package com.huynq.movieapp.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
@Dao
interface FavoriteDAO {
    @Insert
    suspend fun insertFavorite(favorite: FavouriteMovie)

    @Delete
    suspend fun deleteFavourite(favorite: FavouriteMovie)

    @Query("DELETE FROM favourite_movies")
    suspend fun deleteAllFavourite()

    @Query("SELECT * FROM favourite_movies")
    fun getAllFavourite(): Flow<List<FavouriteMovie>>

    @Query("SELECT * FROM favourite_movies WHERE movie_id = :movie_id LIMIT 1")
    suspend fun getMovieById(movie_id: Int):FavouriteMovie?

    @Query("SELECT * FROM favourite_movies WHERE movie_name LIKE '%' || :movie_name || '%'")
    fun getMovieByName(movie_name: String): Flow<List<FavouriteMovie>>
}