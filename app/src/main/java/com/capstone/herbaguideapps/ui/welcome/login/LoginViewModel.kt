package com.capstone.herbaguideapps.ui.welcome.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.herbaguideapps.data.Result
import com.capstone.herbaguideapps.data.model.LoginBody
import com.capstone.herbaguideapps.data.model.LogoutBody
import com.capstone.herbaguideapps.data.remote.response.AuthResponse
import com.capstone.herbaguideapps.data.remote.response.LoginResponse
import com.capstone.herbaguideapps.data.repos.AuthRepository
import com.capstone.herbaguideapps.session.SessionModel
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    val loginResult: LiveData<Result<LoginResponse>> = authRepository.loginResult
    val logoutResult: LiveData<Result<AuthResponse>> = authRepository.authResult

    fun login(loginBody: LoginBody) {
        viewModelScope.launch {
            authRepository.login(loginBody)
        }
    }

    fun logout(logoutBody: LogoutBody) {
        viewModelScope.launch {
            authRepository.logout(logoutBody)
        }
    }

    fun saveSession(sessionModel: SessionModel) {
        viewModelScope.launch {
            authRepository.saveSession(sessionModel)
        }
    }

    fun getSession(): LiveData<SessionModel> {
        return authRepository.getSession().asLiveData()
    }
}