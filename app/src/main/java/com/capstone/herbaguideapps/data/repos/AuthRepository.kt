package com.capstone.herbaguideapps.data.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstone.herbaguideapps.data.Result
import com.capstone.herbaguideapps.data.remote.api.ApiService
import com.capstone.herbaguideapps.data.remote.body.LoginBody
import com.capstone.herbaguideapps.data.remote.body.LogoutBody
import com.capstone.herbaguideapps.data.remote.body.RegisterBody
import com.capstone.herbaguideapps.data.remote.response.AuthResponse
import com.capstone.herbaguideapps.data.remote.response.LoginResponse
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
    private val _authResult = MutableLiveData<Result<AuthResponse>>()
    val authResult: LiveData<Result<AuthResponse>> = _authResult

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    fun getSession(): Flow<SessionModel> {
        return sessionPreferences.getSession()
    }

    suspend fun saveSession(sessionModel: SessionModel) {
        sessionPreferences.saveSession(sessionModel)
    }

    suspend fun login(loginBody: LoginBody) {
        _loginResult.postValue(Result.Loading)
        try {
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
                loginBody.email,
                true,
                loginResponse.token,
                isGuest = false,
                isGoogle = false
            )

            withContext(Dispatchers.IO) {
                sessionPreferences.saveSession(sessionModel)
            }

            _loginResult.postValue(Result.Success(loginResponse))
        } catch (e: Exception) {
            _loginResult.postValue(Result.Error(e.message ?: "Unknown error"))
        }
    }

    suspend fun logout(logoutBody: LogoutBody) {
        _authResult.postValue(Result.Loading)
        try {
            val loginResponse = withContext(Dispatchers.IO) {
                suspendCancellableCoroutine<AuthResponse> { continuation ->
                    val client = apiService.logout(logoutBody)
                    client.enqueue(object : Callback<AuthResponse> {
                        override fun onResponse(
                            call: Call<AuthResponse>,
                            response: Response<AuthResponse>
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

                        override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                            continuation.resumeWithException(t)
                        }
                    })

                    continuation.invokeOnCancellation {
                        client.cancel()
                    }
                }
            }

            withContext(Dispatchers.IO) {
                sessionPreferences.logout()
            }

            _authResult.postValue(Result.Success(loginResponse))
        } catch (e: Exception) {
            _authResult.postValue(Result.Error(e.message ?: "Unknown error"))
        }
    }

    fun register(registerBody: RegisterBody) {
        _authResult.value = Result.Loading
        val client = apiService.register(registerBody)
        client.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    _authResult.value = Result.Success(response.body()!!)
                } else {
                    _authResult.value = Result.Error(response.errorBody()!!.string())
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                _authResult.value = Result.Error(t.message.toString())
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