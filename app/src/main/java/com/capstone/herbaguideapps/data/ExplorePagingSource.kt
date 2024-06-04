package com.capstone.herbaguideapps.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.capstone.herbaguideapps.BuildConfig
import com.capstone.herbaguideapps.data.remote.api.ApiService
import com.capstone.herbaguideapps.data.remote.response.ArticlesItem

class ExplorePagingSource(private val apiService: ApiService) : PagingSource<Int, ArticlesItem>() {
    override fun getRefreshKey(state: PagingState<Int, ArticlesItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticlesItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getHerbalNews(page = position, pageSize = params.loadSize, apikey = BuildConfig.NEWS_KEY)

            Log.d("ExplorePagingSource", "load: ${responseData.status}")
            LoadResult.Page(
                data = responseData.articles,
                prevKey =if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.articles.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            Log.d("ExplorePagingSource", "load: ${e.message}")
            return LoadResult.Error(e)
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}