package com.capstone.herbaguideapps.data.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.capstone.herbaguideapps.data.Result
import com.capstone.herbaguideapps.data.remote.LoginBody
import com.capstone.herbaguideapps.data.remote.RegisterBody
import com.capstone.herbaguideapps.data.remote.api.ApiConfig
import com.capstone.herbaguideapps.data.remote.api.ApiService
import com.capstone.herbaguideapps.data.remote.response.AuthResponse
import com.capstone.herbaguideapps.session.SessionModel
import com.capstone.herbaguideapps.session.SessionPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AuthRepository(
    private val sessionPreferences: SessionPreferences,
    private val apiService: ApiService
) {

    private val result = MediatorLiveData<Result<AuthResponse>>()

    fun getSession(): Flow<SessionModel> {
        return sessionPreferences.getSession()
    }

    suspend fun saveSession(sessionModel: SessionModel) {
        sessionPreferences.saveSession(sessionModel)
    }

    suspend fun logout() {
        sessionPreferences.logout()
    }

    suspend fun login(loginBody: LoginBody): Result<AuthResponse> {
        return try {
            val authResponse = withContext(Dispatchers.IO) {
                suspendCancellableCoroutine<AuthResponse> { continuation ->
                    val client = apiService.login(loginBody)
                    client.enqueue(object : Callback<AuthResponse> {
                        override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                            if (response.isSuccessful) {
                                response.body()?.let {
                                    continuation.resume(it)
                                } ?: continuation.resumeWithException(Exception("Response body is null"))
                            } else {
                                continuation.resumeWithException(Exception(response.errorBody()?.string() ?: "Unknown error"))
                            }
                        }

                        override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                            continuation.resumeWithException(t)
                        }
                    })

                    continuation.invokeOnCancellation {
                        client.cancel()
                    }
                }
            }

            val sessionModel = SessionModel(
                loginBody.email,
                loginBody.email,
                true,
                authResponse.token,
                false
            )

            withContext(Dispatchers.IO) {
                sessionPreferences.saveSession(sessionModel)
            }

            Result.Success(authResponse)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }

    fun register(registerBody: RegisterBody): LiveData<Result<AuthResponse>> {
        result.value = Result.Loading
        val client = ApiConfig.getAuthServices().register(registerBody)
        client.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    result.value = Result.Success(response.body()!!)
                } else {
                    result.value = Result.Error(response.errorBody()!!.string())
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }

        })
        return result
    }

    companion object {
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(
            sessionPreferences: SessionPreferences,
            apiService: ApiService
        ): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(sessionPreferences, apiService)
            }.also { instance = it }
    }}