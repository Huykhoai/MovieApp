package com.huynq.movieapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.huynq.movieapp.data.MovieResponsitory

class MainViewModelFactory(private val repository: MovieResponsitory): ViewModelProvider.Factory {
   override fun <T: ViewModel> create(modelClass: Class<T>): T
   {
    if(modelClass.isAssignableFrom(MainViewModel::class.java)){
       return MainViewModel(repository) as T
    }
       throw IllegalArgumentException("Unknown ViewModel")
   }
}