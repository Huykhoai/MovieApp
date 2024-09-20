package com.huynq.movieapp.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_movies")
data class FavouriteMovie(
    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    val movie_id : Int,
    @ColumnInfo(name = "movie_name")
    val movie_name : String,
    @ColumnInfo(name = "movie_vote")
    val movie_vote: Double,
    @ColumnInfo(name = "image_url")
    val movie_image: String
)