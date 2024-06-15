package com.capstone.herbaguideapps.ui.welcome.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _authResult = MutableLiveData<Result<AuthResponse>>()
    val authResult: LiveData<Result<AuthResponse>> = _authResult

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    fun getSession(): LiveData<SessionModel> = authRepository.getSession().asLiveData()

    fun saveSession(sessionModel: SessionModel) {
        viewModelScope.launch {
            authRepository.saveSession(sessionModel)
        }
    }

    fun login(loginBody: LoginBody) {
        viewModelScope.launch {
            _loginResult.value = Result.Loading
            val result = authRepository.login(loginBody)
            _loginResult.postValue(result)
        }
    }

    fun logout(logoutBody: LogoutBody) {
        viewModelScope.launch {
            _authResult.value = Result.Loading
            authRepository.logout(logoutBody) { result ->
                _authResult.postValue(result)
            }
        }
    }
}