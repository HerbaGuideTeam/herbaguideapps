package com.capstone.herbaguideapps.di

import android.content.Context
import com.capstone.herbaguideapps.data.remote.api.ApiConfig
import com.capstone.herbaguideapps.data.repos.AuthRepository
import com.capstone.herbaguideapps.data.repos.ExploreRepository
import com.capstone.herbaguideapps.session.SessionPreferences
import com.capstone.herbaguideapps.session.dataStore

object Injection {
    fun provideExploreRepository(context: Context): ExploreRepository {
        val apiService = ApiConfig.getNewsApiService()
        return ExploreRepository.getInstance(apiService)
    }

    fun provideAuthRepository(context: Context): AuthRepository {
        val apiService = ApiConfig.getAuthServices()
        val sessionPreferences = SessionPreferences.getInstance(context.dataStore)
        return AuthRepository.getInstance(sessionPreferences, apiService)
    }
}