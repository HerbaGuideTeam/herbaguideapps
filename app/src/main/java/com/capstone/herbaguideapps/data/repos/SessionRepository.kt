package com.capstone.herbaguideapps.data.repos

import com.capstone.herbaguideapps.session.SessionModel
import com.capstone.herbaguideapps.session.SessionPreferences
import kotlinx.coroutines.flow.Flow

class SessionRepository(
    private val sessionPreferences: SessionPreferences
) {

    fun getSession(): Flow<SessionModel> {
        return sessionPreferences.getSession()
    }

    suspend fun logout() {
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