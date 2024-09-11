package com.huynq.movieapp.retrofit

import com.huynq.movieapp.model.MovieDetails
import com.huynq.movieapp.model.MovieResponse
import com.huynq.movieapp.model.ResponseCast
import com.huynq.movieapp.model.VideoResponse
import com.huynq.movieapp.utils.APIConstants
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET(APIConstants.POPULAR_MOVIES)
    suspend fun getPopularMovies(@Query("api_key") api_key: String): Response<MovieResponse>

    @GET(APIConstants.UPCOMMING_MOVIES)
    suspend fun getUpCommingMovies(@Query("api_key") api_key: String): Response<MovieResponse>

    @GET(APIConstants.MOVIE_DETAILS)
    suspend fun getDetailsMovies(@Path("movie_id") movie_id: Int, @Query("api_key") api_key: String): Response<MovieDetails>

    @GET(APIConstants.MOVIE_CREDITS)
    suspend fun getMovieCredits(@Path("movie_id") movie_id: Int, @Query("api_key") api_key: String) : Response<ResponseCast>

    @GET(APIConstants.VIDEO_DETAILS)
    suspend fun getVideoDetails(@Path("movie_id") movie_id: Int, @Query("api_key") api_key: String) : Response<VideoResponse>
}