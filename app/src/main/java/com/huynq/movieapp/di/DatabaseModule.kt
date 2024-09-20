package com.huynq.movieapp.di

import android.content.Context
import com.huynq.movieapp.room.FavoriteDAO
import com.huynq.movieapp.room.FavouriteMovieDb
import com.huynq.movieapp.room.FavouriteMovieResponsitory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FavouriteMovieDb {
        return FavouriteMovieDb.getInstance(context)
    }
    @Provides
    @Singleton
    fun provideFavouriteDao(database: FavouriteMovieDb): FavoriteDAO{
        return database.favouriteDao
    }
}