package com.capstone.herbaguideapps.ui.welcome.register

import androidx.lifecycle.ViewModel
import com.capstone.herbaguideapps.data.remote.body.RegisterBody
import com.capstone.herbaguideapps.data.repos.AuthRepository

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    fun register(registerBody: RegisterBody) = authRepository.register(registerBody)
}