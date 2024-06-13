package com.capstone.herbaguideapps.data.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstone.herbaguideapps.data.Result
import com.capstone.herbaguideapps.data.remote.response.AuthResponse
import com.capstone.herbaguideapps.session.SessionModel
import com.capstone.herbaguideapps.session.SessionPreferences
import kotlinx.coroutines.flow.Flow

class SessionRepository(
    private val sessionPreferences: SessionPreferences
) {

    private val _logoutResult = MutableLiveData<Result<AuthResponse>>()
    val logoutResult: LiveData<Result<AuthResponse>> = _logoutResult

    fun getSession(): Flow<SessionModel> {
        return sessionPreferences.getSession()
    }

    suspend fun logoutSession() {
        sessionPreferences.logout()
    }

    companion object {
        @Volatile
        private var instance: SessionRepository? = null
        fun getInstance(
            sessionPreferences: SessionPreferences
        ): SessionRepository =
            instance ?: synchronized(this) {
                instance ?: SessionRepository(sessionPreferences)
            }.also { instance = it }
    }
}