package com.huynq.movieapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.huynq.movieapp.model.Movies
import com.huynq.movieapp.pages.PageSource
import com.huynq.movieapp.retrofit.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject
@HiltViewModel
class MovieViewModel @Inject constructor(apiService: ApiService): ViewModel(){
    val moviesResult : Flow<PagingData<Movies>> =
        Pager(config = PagingConfig(10, enablePlaceholders = false)){
            PageSource(apiService)
        }.flow.cachedIn(viewModelScope)
            .catch {
                Log.e("MoviesViewModel", ":${it} ", )
            }
}