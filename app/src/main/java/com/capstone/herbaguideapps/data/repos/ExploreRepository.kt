package com.capstone.herbaguideapps.data.repos

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.capstone.herbaguideapps.data.ExplorePagingSource
import com.capstone.herbaguideapps.data.remote.api.ApiService
import com.capstone.herbaguideapps.data.remote.response.ArticlesItem

class ExploreRepository private constructor(
    private val apiService: ApiService
) {
    fun getHeadlineNews(): LiveData<PagingData<ArticlesItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20
            ),
            pagingSourceFactory = {
                ExplorePagingSource(apiService)
            }
        ).liveData
    }

    companion object {
        @Volatile
        private var instance: ExploreRepository? = null
        fun getInstance(
            apiService: ApiService
        ): ExploreRepository =
            instance ?: synchronized(this) {
                instance ?: ExploreRepository(apiService)
            }.also { instance = it }
    }
}