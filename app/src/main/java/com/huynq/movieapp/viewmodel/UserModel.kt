package com.huynq.movieapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huynq.movieapp.data.UserRepository
import com.huynq.movieapp.model.UserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class UserModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    var _loginMultableLiveData = MutableLiveData<Result<UserResponse>>()
    val loginMultableLiveData : LiveData<Result<UserResponse>> get() = _loginMultableLiveData
    var _error = MutableLiveData<Result<String>>()
    val error : LiveData<Result<String>> get() = _error


    fun login(email: String , password: String){
        viewModelScope.launch {
            userRepository.login(email, password)
                .catch { e ->
                    _loginMultableLiveData.value = Result.failure(e)
                }
                    .collect{
                    _loginMultableLiveData.value = Result.success(it)
                }
        }
    }
}