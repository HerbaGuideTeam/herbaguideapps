package com.capstone.herbaguideapps.ui.identify

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.capstone.herbaguideapps.data.Result
import com.capstone.herbaguideapps.data.remote.response.PredictResponse
import com.capstone.herbaguideapps.data.repos.PredictRepository
import okhttp3.MultipartBody

class PredictViewModel(
    private val predictRepository: PredictRepository
) : ViewModel() {
    private val _predictResult = MediatorLiveData<Result<PredictResponse>>()
    val predictResult: LiveData<Result<PredictResponse>> = _predictResult

    fun predictImage(part: MultipartBody.Part, isGuest: Boolean) {
        _predictResult.value = Result.Loading
        predictRepository.predictImage(part, isGuest) { result ->
            _predictResult.postValue(result)
        }
    }
}