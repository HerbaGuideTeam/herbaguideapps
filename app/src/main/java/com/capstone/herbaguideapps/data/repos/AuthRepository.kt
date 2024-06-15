package com.capstone.herbaguideapps.data.repos

import android.util.Log
import com.capstone.herbaguideapps.data.Result
import com.capstone.herbaguideapps.data.model.LoginBody
import com.capstone.herbaguideapps.data.model.LogoutBody
import com.capstone.herbaguideapps.data.model.RegisterBody
import com.capstone.herbaguideapps.data.remote.api.ApiService
import com.capstone.herbaguideapps.data.remote.response.AuthResponse
import com.capstone.herbaguideapps.data.remote.response.LoginResponse
import com.capstone.herbaguideapps.session.SessionModel
import com.capstone.herbaguideapps.session.SessionPreferences
import kotlinx.coroutines.Dispatchers
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
    fun getSession() = sessionPreferences.getSession()

    suspend fun saveSession(sessionModel: SessionModel) {
        sessionPreferences.saveSession(sessionModel)
    }

    suspend fun login(loginBody: LoginBody): Result<LoginResponse> {
        return try {
            val loginResponse = withContext(Dispatchers.IO) {
                suspendCancellableCoroutine<LoginResponse> { continuation ->
                    val client = apiService.login(loginBody)
                    client.enqueue(object : Callback<LoginResponse> {
                        override fun onResponse(
                            call: Call<LoginResponse>,
                            response: Response<LoginResponse>
                        ) {
                            if (response.isSuccessful) {
                                response.body()?.let {
                                    continuation.resume(it)
                                }
                                    ?: continuation.resumeWithException(Exception("Response body is null"))
                            } else {
                                continuation.resumeWithException(
                                    Exception(response.errorBody()?.string() ?: "Unknown error")
                                )
                            }
                        }

                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            continuation.resumeWithException(t)
                        }
                    })

                    continuation.invokeOnCancellation {
                        client.cancel()
                    }
                }
            }
            val sessionModel = SessionModel(
                loginResponse.user.displayName,
                loginResponse.user.email,
                true,
                loginResponse.token,
                isGuest = false
            )

            Log.d("AuthRepository", "login: ${loginResponse.token}")

            withContext(Dispatchers.IO) {
                sessionPreferences.saveSession(sessionModel)
            }

            Result.Success(loginResponse)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }

    fun logout(logoutBody: LogoutBody, onResult: (Result<AuthResponse>) -> Unit) {
        val client = apiService.logout(logoutBody)
        client.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(
                call: Call<AuthResponse>,
                response: Response<AuthResponse>
            ) {
                if (response.isSuccessful) {
                    onResult(Result.Success(response.body()!!))
                } else {
                    onResult(Result.Error(response.errorBody()!!.string()))
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                onResult(Result.Error(t.message.toString()))
            }
        })
    }

    fun register(registerBody: RegisterBody, onResult: (Result<AuthResponse>) -> Unit) {
        val client = apiService.register(registerBody)
        client.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(
                call: Call<AuthResponse>,
                response: Response<AuthResponse>
            ) {
                if (response.isSuccessful) {
                    onResult(Result.Success(response.body()!!))
                } else {
                    onResult(Result.Error(response.errorBody()!!.string()))
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                onResult(Result.Error(t.message.toString()))
            }
        })
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
    }
}
