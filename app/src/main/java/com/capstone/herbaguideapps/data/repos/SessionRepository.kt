package com.capstone.herbaguideapps.data.repos

import com.capstone.herbaguideapps.data.Result
import com.capstone.herbaguideapps.data.remote.api.ApiService
import com.capstone.herbaguideapps.data.remote.response.ValidateResponse
import com.capstone.herbaguideapps.session.SessionModel
import com.capstone.herbaguideapps.session.SessionPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SessionRepository(
    private val sessionPreferences: SessionPreferences,
    private val apiService: ApiService
) {
    fun getSession(): Flow<SessionModel> {
        return sessionPreferences.getSession()
    }

    suspend fun logoutSession() {
        sessionPreferences.logout()
    }

    fun validateToken(onResult: (Result<ValidateResponse>) -> Unit) {
        val token = runBlocking {
            sessionPreferences.getToken().first()
        }
        val client = apiService.validateToken(token)
        client.enqueue(object : Callback<ValidateResponse> {
            override fun onResponse(
                call: Call<ValidateResponse>,
                response: Response<ValidateResponse>
            ) {
                if (response.isSuccessful) {
                    onResult(Result.Success(response.body()!!))
                } else {
                    onResult(Result.Error(response.errorBody()!!.string()))
                }
            }

            override fun onFailure(call: Call<ValidateResponse>, t: Throwable) {
                onResult(Result.Error(t.message.toString()))
            }
        })
    }


    companion object {
        @Volatile
        private var instance: SessionRepository? = null
        fun getInstance(
            sessionPreferences: SessionPreferences,
            apiService: ApiService
        ): SessionRepository =
            instance ?: synchronized(this) {
                instance ?: SessionRepository(sessionPreferences, apiService)
            }.also { instance = it }
    }
}