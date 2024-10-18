package com.huynq.movieapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huynq.movieapp.data.UserRepository
import com.huynq.movieapp.model.Movies
import com.huynq.movieapp.model.UserResponse
import com.huynq.movieapp.model.WatchListResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class UserModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    var _loginMultableLiveData = MutableLiveData<Result<UserResponse>>()
    val loginMultableLiveData: LiveData<Result<UserResponse>> get() = _loginMultableLiveData
    var _user = MutableLiveData<UserResponse>()
    val user: LiveData<UserResponse> get() = _user
    var _watchList = MutableLiveData<WatchListResponse>()
    val watchList: LiveData<WatchListResponse> get() = _watchList
    var _addWatchList = MutableLiveData<Result<WatchListResponse>>()
    val addWatchList: LiveData<Result<WatchListResponse>> get() = _addWatchList
    var _change_password = MutableLiveData<Result<UserResponse>>()
    val change_password: LiveData<Result<UserResponse>> get() = _change_password


    fun login(email: String, password: String) {
        viewModelScope.launch {
            userRepository.login(email, password)
                .catch { e ->
                    _loginMultableLiveData.value = Result.failure(e)
                }
                .collect {
                    _loginMultableLiveData.value = Result.success(it)
                }
        }
    }

    fun get_user(id: String) {
        viewModelScope.launch {
            userRepository.get_user(id)
                .catch {
                    Log.d("Huy", "get_user: ${it.message}")
                }
                .collect {
                    _user.value = it
                }
        }
    }

    fun watch_list() {
        viewModelScope.launch {
            userRepository.watch_list()
                .catch {
                    Log.d("Huy", "watch_list:${it} ")
                }
                .collect {
                    _watchList.value = it
                }
        }
    }

    fun add_watch_list(movie: Movies) {
        viewModelScope.launch {
            userRepository.add_watch_list(movie)
                .catch {
                    _addWatchList.value = Result.failure(it)
                }
                .collect {
                    _addWatchList.value = Result.success(it)
                }
        }
    }
    fun change_password(email: String, oldPassword: String, newPassword: String){
        viewModelScope.launch {
            userRepository.change_password(email,oldPassword,newPassword)
                .catch {
                    _change_password.value = Result.failure(it)
                }
                .collect{
                    _change_password.value = Result.success(it)
                }
        }
    }
}
