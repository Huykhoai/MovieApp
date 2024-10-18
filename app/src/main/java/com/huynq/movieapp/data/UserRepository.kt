package com.huynq.movieapp.data

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.huynq.movieapp.model.Movies
import com.huynq.movieapp.model.ObjectError
import com.huynq.movieapp.model.UserResponse
import com.huynq.movieapp.model.WatchListResponse
import com.huynq.movieapp.request.LoginRequest
import com.huynq.movieapp.request.RequestChangePass
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
     fun get_user(id: String): Flow<UserResponse> = flow{
     val response = userService.getUser(id)
        if(response.isSuccessful){
            emit(response.body()!!)
        }
    }
    fun watch_list(): Flow<WatchListResponse> = flow{
        val response = userService.watchList()
        if(response.isSuccessful){
            emit(response.body()!!)
        }
    }
    fun add_watch_list(movie: Movies): Flow<WatchListResponse> = flow{
        val response = userService.addWatchList(movie)
        if(response.isSuccessful){
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
    fun change_password(email: String, oldPassword: String, newPassword: String): Flow<UserResponse> = flow{
        val response = userService.changePassword(RequestChangePass(email,oldPassword,newPassword))
        if(response.isSuccessful){
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