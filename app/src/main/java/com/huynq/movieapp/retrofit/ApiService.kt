package com.huynq.movieapp.retrofit

import com.huynq.movieapp.model.MovieDetails
import com.huynq.movieapp.model.MovieResponse
import com.huynq.movieapp.model.ResponseCast
import com.huynq.movieapp.model.SearchResponse
import com.huynq.movieapp.model.VideoResponse
import com.huynq.movieapp.utils.APIConstants
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET(APIConstants.POPULAR_MOVIES)
    suspend fun getPopularMovies(@Query("api_key") api_key: String, @Query("page") page: String)
    : Response<MovieResponse>

    @GET(APIConstants.UPCOMMING_MOVIES)
    suspend fun getUpCommingMovies(@Query("api_key") api_key: String, @Query("page") page: String)
    : Response<MovieResponse>

    @GET(APIConstants.MOVIE_DETAILS)
    suspend fun getDetailsMovies(@Path("movie_id") movie_id: Int, @Query("api_key") api_key: String)
    : Response<MovieDetails>

    @GET(APIConstants.MOVIE_CREDITS)
    suspend fun getMovieCredits(@Path("movie_id") movie_id: Int, @Query("api_key") api_key: String)
    : Response<ResponseCast>

    @GET(APIConstants.VIDEO_DETAILS)
    suspend fun getVideoDetails(@Path("movie_id") movie_id: Int, @Query("api_key") api_key: String)
    : Response<VideoResponse>
    @GET(APIConstants.MOVIE_SEARCH)
    suspend fun searchMovies(@Query("query") query: String, @Query("api_key") api_key: String)
    : Response<SearchResponse>
    @GET(APIConstants.DISCOVER_MOVIES)
    suspend fun getDiscoverMovies(@Query("api_key") api_key: String, @Query("page") page: Int)
    : Response<MovieResponse>
    @GET(APIConstants.TOP_RATED_MOVIES)
    suspend fun getToprateMovie(@Query("api_key") api_key: String, @Query("page") page: String)
            : Response<MovieResponse>
    @GET(APIConstants.NOW_PLAYING)
    suspend fun getNowPlaying(@Query("api_key") api_key: String, @Query("page") page: String)
            : Response<MovieResponse>
}