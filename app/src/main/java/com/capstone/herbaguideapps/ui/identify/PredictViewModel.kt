package com.capstone.herbaguideapps.ui.identify

import androidx.lifecycle.ViewModel
import com.capstone.herbaguideapps.data.repos.PredictRepository
import okhttp3.MultipartBody

class PredictViewModel(
    private val predictRepository: PredictRepository
) : ViewModel() {
    fun analyzeImage(part: MultipartBody.Part, isGuest: Boolean) =
        predictRepository.predictImage(part, isGuest)
}