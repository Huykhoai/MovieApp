package com.huynq.movieapp.request

data class RequestChangePass(
    val email: String,
    val old_password: String,
    val new_password: String

)