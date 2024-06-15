package com.capstone.herbaguideapps.data.repos

import android.util.Log
import com.capstone.herbaguideapps.data.Result
import com.capstone.herbaguideapps.data.remote.api.ApiService
import com.capstone.herbaguideapps.data.remote.response.HistoryResponse
import com.capstone.herbaguideapps.data.remote.response.PredictResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PredictRepository private constructor(
    private val apiService: ApiService
) {
    fun predictImage(
        part: MultipartBody.Part,
        isGuest: Boolean,
        onResult: (Result<PredictResponse>) -> Unit
    ) {
        val client: Call<PredictResponse> = if (isGuest) {
            apiService.uploadImageAnon(part)
        } else {
            apiService.uploadImage(part)
        }

        client.enqueue(object : Callback<PredictResponse> {
            override fun onResponse(
                call: Call<PredictResponse>,
                response: Response<PredictResponse>
            ) {
                if (response.isSuccessful) {
                    onResult(Result.Success(response.body()!!))
                } else {
                    onResult(Result.Error(response.errorBody()!!.string()))
                }
            }

            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                onResult(Result.Error(t.message.toString()))
            }
        })
    }

    fun getHistory(onResult: (Result<HistoryResponse>) -> Unit) {
        val client = apiService.history()
        client.enqueue(object : Callback<HistoryResponse> {
            override fun onResponse(
                call: Call<HistoryResponse>,
                response: Response<HistoryResponse>
            ) {
                if (response.isSuccessful) {
                    onResult(Result.Success(response.body()!!))
                    Log.d("PredictRepository", "onResponse: ${response.body()?.message}")
                } else {
                    onResult(Result.Error(response.errorBody()!!.string()))
                    Log.d("PredictRepository", "onResponse: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                onResult(Result.Error(t.message.toString()))
                Log.d("PredictRepository", "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        @Volatile
        private var instance: PredictRepository? = null
        fun getInstance(apiService: ApiService): PredictRepository =
            instance ?: synchronized(this) {
                instance ?: PredictRepository(apiService)
            }.also { instance = it }
    }
}