package com.capstone.herbaguideapps.session

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.herbaguideapps.data.Result
import com.capstone.herbaguideapps.data.remote.response.ValidateResponse
import com.capstone.herbaguideapps.data.repos.SessionRepository
import kotlinx.coroutines.launch

class SessionViewModel(private val sessionRepository: SessionRepository) : ViewModel() {

    private val _authResult = MutableLiveData<Result<ValidateResponse>>()
    val authResult: LiveData<Result<ValidateResponse>> = _authResult

    fun validateToken() {
        viewModelScope.launch {
            _authResult.value = Result.Loading
            sessionRepository.validateToken { result ->
                _authResult.postValue(result)
                Log.d("SessionViewModel", "validateToken: $result")
            }
        }
    }

    fun getSession(): LiveData<SessionModel> {
        return sessionRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            sessionRepository.logoutSession()
        }
    }
}