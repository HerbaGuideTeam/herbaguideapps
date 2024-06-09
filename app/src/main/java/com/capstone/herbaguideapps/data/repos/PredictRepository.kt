package com.capstone.herbaguideapps.data.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.capstone.herbaguideapps.data.Result
import com.capstone.herbaguideapps.data.remote.api.ApiService
import com.capstone.herbaguideapps.data.remote.response.FileUploadResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PredictRepository private constructor(
    private val apiService: ApiService
) {

    private val result = MediatorLiveData<Result<FileUploadResponse>>()
    fun predictImage(part: MultipartBody.Part): LiveData<Result<FileUploadResponse>> {
        result.value = Result.Loading
        val client = apiService.uploadImage(part)
        client.enqueue(object : Callback<FileUploadResponse> {
            override fun onResponse(
                call: Call<FileUploadResponse>,
                response: Response<FileUploadResponse>
            ) {
                if (response.isSuccessful) {
                    result.value = Result.Success(response.body()!!)
                } else {
                    result.value = Result.Error(response.errorBody()!!.string())
                }
            }

            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }

        })
        return result
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