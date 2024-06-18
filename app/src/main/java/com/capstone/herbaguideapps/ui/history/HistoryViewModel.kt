package com.capstone.herbaguideapps.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.herbaguideapps.data.Result
import com.capstone.herbaguideapps.data.remote.response.HistoryResponse
import com.capstone.herbaguideapps.data.repos.PredictRepository
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val predictRepository: PredictRepository
) : ViewModel() {

    private val _historyResult = MutableLiveData<Result<HistoryResponse>>()
    val historyResult: LiveData<Result<HistoryResponse>> = _historyResult

    fun getHistory() {
        _historyResult.value = Result.Loading
        viewModelScope.launch {
            predictRepository.getHistory { result ->
                _historyResult.postValue(result)
            }
        }
    }

    fun searchHistory(search: String) {
        _historyResult.value = Result.Loading
        viewModelScope.launch {
            predictRepository.searchHistory(search) { result ->
                _historyResult.postValue(result)
            }
        }
    }
}