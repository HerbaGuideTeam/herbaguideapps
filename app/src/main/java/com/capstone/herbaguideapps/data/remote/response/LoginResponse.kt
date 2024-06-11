package com.capstone.herbaguideapps.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("user")
    val user: User,

    @field:SerializedName("token")
    val token: String
)

data class User(

    @field:SerializedName("uid")
    val uid: String,

    @field:SerializedName("display_name")
    val displayName: String,

    @field:SerializedName("email")
    val email: String
)
