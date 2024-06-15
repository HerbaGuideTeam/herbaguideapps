package com.capstone.herbaguideapps.di

import android.content.Context
import com.capstone.herbaguideapps.data.remote.api.ApiConfig
import com.capstone.herbaguideapps.data.repos.AuthRepository
import com.capstone.herbaguideapps.data.repos.ExploreRepository
import com.capstone.herbaguideapps.data.repos.PredictRepository
import com.capstone.herbaguideapps.data.repos.SessionRepository
import com.capstone.herbaguideapps.session.SessionPreferences
import com.capstone.herbaguideapps.session.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

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

    fun providePredictRepository(context: Context): PredictRepository {
        val sessionPreferences = SessionPreferences.getInstance(context.dataStore)
        val token = runBlocking { sessionPreferences.getToken().first() }
        val apiService = ApiConfig.getApiServices(token)
        return PredictRepository.getInstance(apiService)
    }

    fun provideSessionRepository(context: Context): SessionRepository {
        val sessionPreferences = SessionPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getAuthServices()
        return SessionRepository.getInstance(sessionPreferences, apiService)
    }

}