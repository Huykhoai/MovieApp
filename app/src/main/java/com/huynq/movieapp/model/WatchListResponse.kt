package com.huynq.movieapp.model

data class WatchListResponse(
    val status: Int,
    val message: String,
    val data: List<Movies>
)
