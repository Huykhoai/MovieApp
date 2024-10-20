package com.huynq.movieapp.utils

object APIConstants {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val API_KEY = "ce99ea84f89451260059c832125c6353"
    const val DISCOVER_MOVIES = "discover/movie"
    const val POPULAR_MOVIES = "movie/popular"
    const val UPCOMMING_MOVIES = "movie/upcoming"
    const val GENRE_MOVIE_LIST = "genre/movie/list"
    const val MOVIE_DETAILS = "movie/{movie_id}"
    const val IMAGE_PATH = "https://image.tmdb.org/t/p/w500"
    const val MOVIE_CREDITS = "movie/{movie_id}/credits"
    const val VIDEO_DETAILS = "movie/{movie_id}/videos"
    const val YOUTUBE_URL = "https://www.youtube.com/embed/"
    const val MOVIE_SEARCH = "search/movie"
    const val TOP_RATED_MOVIES = "movie/top_rated"
    const val NOW_PLAYING = "movie/now_playing"

    const val LOGIN = "/api/post/login_user"
    const val RESISTER = "/api/post/create_user"
    const val get_user = "/api/get/get_user/{id}"
    const val add_watch_list = "/api/post/add_watchlist"
    const val watch_list = "/api/get/watchlist"
    const val change_avatar = "/api/post/change_avatar"
    const val change_password = "/api/post/change_password"

}