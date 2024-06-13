package com.capstone.herbaguideapps.data.repos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
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

    private val _historyResult = MutableLiveData<Result<HistoryResponse>>()
    val historyResult: LiveData<Result<HistoryResponse>> = _historyResult

    private val result = MediatorLiveData<Result<PredictResponse>>()

    fun predictImage(
        part: MultipartBody.Part,
        isGuest: Boolean
    ): LiveData<Result<PredictResponse>> {
        result.value = Result.Loading
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
                    result.value = Result.Success(response.body()!!)
                } else {
                    result.value = Result.Error(response.errorBody()!!.string())
                }
            }

            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }

        })
        return result
    }

    fun getHistory() {
        _historyResult.value = Result.Loading
        val client = apiService.history()
        client.enqueue(object : Callback<HistoryResponse> {
            override fun onResponse(
                call: Call<HistoryResponse>,
                response: Response<HistoryResponse>
            ) {
                if (response.isSuccessful) {
                    _historyResult.value = Result.Success(response.body()!!)
                    Log.d("PredictRepository", "onResponse: ${response.body()?.message}")
                } else {
                    _historyResult.value = Result.Error(response.errorBody()!!.string())
                    Log.d("PredictRepository", "onResponse: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                _historyResult.value = Result.Error(t.message.toString())
                Log.d("PredictRepository", "onFailure: ${t.message}")
            }

        })
    }

    companion object {
        @Volatile
        private var instance: PredictRepository? = null
        fun getInstance(
            apiService: ApiService
        ): PredictRepository =
            instance ?: synchronized(this) {
                instance ?: PredictRepository(apiService)
            }.also { instance = it }
    }
}