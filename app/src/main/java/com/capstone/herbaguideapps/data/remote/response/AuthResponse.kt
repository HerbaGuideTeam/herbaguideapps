package com.capstone.herbaguideapps.data.remote.response

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @field:SerializedName("message")
    val message: String
)
