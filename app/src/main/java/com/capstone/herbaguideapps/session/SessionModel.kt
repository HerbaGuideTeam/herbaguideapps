package com.capstone.herbaguideapps.session

data class SessionModel(
    val name: String,
    val email: String,
    val isLogin: Boolean,
    val token: String,
    val isGuest: Boolean,
    val isGoogle: Boolean
)
