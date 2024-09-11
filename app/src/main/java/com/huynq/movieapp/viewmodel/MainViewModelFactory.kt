package com.huynq.movieapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.huynq.movieapp.data.MovieResponsitory

class MainViewModelFactory(private val responsitory: MovieResponsitory): ViewModelProvider.Factory {
   override fun <T: ViewModel> create(modelClass: Class<T>): T
   {
    if(modelClass.isAssignableFrom(MainViewModel::class.java)){
       return MainViewModel(responsitory) as T
    }
       throw IllegalArgumentException("Unknown ViewModel")
   }
}