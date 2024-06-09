package com.capstone.herbaguideapps.data.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.capstone.herbaguideapps.BuildConfig
import com.capstone.herbaguideapps.data.ExplorePagingSource
import com.capstone.herbaguideapps.data.Result
import com.capstone.herbaguideapps.data.remote.api.ApiService
import com.capstone.herbaguideapps.data.remote.response.ArticlesItem
import com.capstone.herbaguideapps.data.remote.response.NewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExploreRepository private constructor(
    private val apiService: ApiService
) {
    private val result = MediatorLiveData<Result<NewsResponse>>()
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


    fun getTopHeadlineNews(): LiveData<Result<NewsResponse>> {
        result.value = Result.Loading
        val client = apiService.topHeadline(BuildConfig.NEWS_KEY)
        client.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    result.value = Result.Success(response.body()!!)
                } else {
                    result.value = Result.Error(response.errorBody()!!.string())
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })
        return result
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