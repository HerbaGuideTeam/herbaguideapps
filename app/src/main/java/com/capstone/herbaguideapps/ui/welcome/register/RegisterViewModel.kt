package com.capstone.herbaguideapps.ui.welcome.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.herbaguideapps.data.Result
import com.capstone.herbaguideapps.data.remote.body.RegisterBody
import com.capstone.herbaguideapps.data.remote.response.AuthResponse
import com.capstone.herbaguideapps.data.repos.AuthRepository
import com.capstone.herbaguideapps.session.SessionModel
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    val authResult: LiveData<Result<AuthResponse?>> = authRepository.authResult

    fun register(registerBody: RegisterBody) {
        viewModelScope.launch {
            authRepository.register(registerBody)
        }
    }
}