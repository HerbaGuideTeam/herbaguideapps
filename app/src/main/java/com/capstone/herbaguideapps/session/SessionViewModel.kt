package com.capstone.herbaguideapps.session

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.herbaguideapps.data.repos.SessionRepository
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch

class SessionViewModel(private val sessionRepository: SessionRepository) : ViewModel() {

    fun getToken(): LiveData<String> {
        return sessionRepository.getToken().asLiveData()
    }

    fun getSession(): LiveData<SessionModel> {
        return sessionRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            sessionRepository.logout()
        }
    }
}