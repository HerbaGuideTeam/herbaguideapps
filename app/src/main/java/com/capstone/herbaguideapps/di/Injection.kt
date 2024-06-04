package com.capstone.herbaguideapps.di

import android.content.Context
import com.capstone.herbaguideapps.data.remote.api.ApiConfig
import com.capstone.herbaguideapps.data.repos.ExploreRepository

object Injection {
    fun provideExploreRepository(context: Context): ExploreRepository {
        val apiService = ApiConfig.getNewsApiService()
        return ExploreRepository.getInstance(apiService)
    }
}