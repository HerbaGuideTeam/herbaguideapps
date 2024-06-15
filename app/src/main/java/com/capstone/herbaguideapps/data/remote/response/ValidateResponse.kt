package com.capstone.herbaguideapps.data.remote.response

import com.google.gson.annotations.SerializedName

data class ValidateResponse(

    @field:SerializedName("user_id")
    val userId: String,

    @field:SerializedName("token")
    val token: String
)
