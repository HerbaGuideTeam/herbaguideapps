package com.capstone.herbaguideapps.ui.welcome.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.herbaguideapps.data.Result
import com.capstone.herbaguideapps.data.remote.LoginBody
import com.capstone.herbaguideapps.data.remote.response.AuthResponse
import com.capstone.herbaguideapps.data.repos.AuthRepository
import com.capstone.herbaguideapps.session.SessionModel
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _loginResult = MutableLiveData<Result<AuthResponse>>()
    val loginResult: LiveData<Result<AuthResponse>> get() = _loginResult

    fun login(loginBody: LoginBody) {
        _loginResult.value = Result.Loading

        viewModelScope.launch {
            val result = authRepository.login(loginBody)
            _loginResult.postValue(result)
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