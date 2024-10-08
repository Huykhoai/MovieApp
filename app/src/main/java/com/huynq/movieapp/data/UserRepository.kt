package com.huynq.movieapp.data

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.huynq.movieapp.model.ObjectError
import com.huynq.movieapp.model.UserResponse
import com.huynq.movieapp.request.LoginRequest
import com.huynq.movieapp.retrofit.ApiService
import com.huynq.movieapp.retrofit.ApiUser
import com.huynq.movieapp.retrofit.ApiUserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserRepository @Inject constructor() {
    val userService = ApiUser.createService(ApiUserService::class.java)

    fun login(email: String, password: String) : Flow<UserResponse> = flow {
        var response = userService.login(LoginRequest(email,password))
        if(response.isSuccessful) {
            emit(response.body()!!)
        }else{
            val errorResponse = response.errorBody()?.string()?.let {
                try {
                    Gson().fromJson(it, ObjectError::class.java)
                }catch (e: JsonSyntaxException){
                    null
                }
            }
            throw Exception(errorResponse?.message ?: "Unknown error")
        }
    }.flowOn(Dispatchers.IO)
}