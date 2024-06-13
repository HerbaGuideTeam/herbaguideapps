package com.capstone.herbaguideapps.data.remote.response

import com.google.gson.annotations.SerializedName

data class PredictResponse(

    @field:SerializedName("confidence")
    val confidence: Float,

    @field:SerializedName("prediction")
    val prediction: Prediction,

    @field:SerializedName("message")
    val message: String
)

data class Prediction(

    @field:SerializedName("tanaman_herbal")
    val tanamanHerbal: TanamanHerbal
)
