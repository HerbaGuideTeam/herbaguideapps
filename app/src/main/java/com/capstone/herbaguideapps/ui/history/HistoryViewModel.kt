package com.capstone.herbaguideapps.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.herbaguideapps.data.Result
import com.capstone.herbaguideapps.data.remote.response.HistoryResponse
import com.capstone.herbaguideapps.data.repos.PredictRepository
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val predictRepository: PredictRepository
) : ViewModel() {

    val history: LiveData<Result<HistoryResponse>> = predictRepository.historyResult

    fun getHistory() {
        viewModelScope.launch {
            predictRepository.getHistory()
        }
    }
}