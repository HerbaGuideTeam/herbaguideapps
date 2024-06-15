package com.capstone.herbaguideapps.ui.welcome.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.herbaguideapps.data.Result
import com.capstone.herbaguideapps.data.model.RegisterBody
import com.capstone.herbaguideapps.data.remote.response.AuthResponse
import com.capstone.herbaguideapps.data.repos.AuthRepository
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _authResult = MutableLiveData<Result<AuthResponse>>()
    val authResult: LiveData<Result<AuthResponse>> = _authResult

    fun register(registerBody: RegisterBody) {
        viewModelScope.launch {
            _authResult.value = Result.Loading
            authRepository.register(registerBody) { result ->
                _authResult.postValue(result)
            }
        }
    }
}