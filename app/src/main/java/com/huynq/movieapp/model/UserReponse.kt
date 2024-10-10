package com.huynq.movieapp.model

data class UserResponse(
    val status: Int,
    val message: String,
    val user: User
)

data class User(
    val _id: String,
    val username: String,
    val email: String,
    val password: String,
    val avatar: String,
    val __v: Int
)