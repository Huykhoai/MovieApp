package com.huynq.movieapp.model

data class VideoResponse (
    val results : List<Videos>,
    private val id: Int
)