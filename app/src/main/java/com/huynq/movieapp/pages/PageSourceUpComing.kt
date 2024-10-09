package com.huynq.movieapp.pages

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.huynq.movieapp.model.Movies
import com.huynq.movieapp.retrofit.ApiService
import com.huynq.movieapp.utils.APIConstants
import okio.IOException

class PageSourceUpComing(private var apiService: ApiService) : PagingSource<Int, Movies>() {
    private val DEFAULT_PAGE_INDEX = 1
    override fun getRefreshKey(state: PagingState<Int, Movies>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1)?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movies> {
        var page = params.key?: DEFAULT_PAGE_INDEX
        return try {
            val response = apiService.getUpCommingMovies(APIConstants.API_KEY, page)
            LoadResult.Page(
                response.body()!!.results,
                prevKey = if(page == DEFAULT_PAGE_INDEX) null else page - 1,
                nextKey = if(response.body() == null) null else page + 1
            )
        }catch (e:IOException){
            LoadResult.Error(e)

        }
    }
}