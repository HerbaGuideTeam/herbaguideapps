package com.capstone.herbaguideapps.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.herbaguideapps.data.local.HistoryEntity
import com.capstone.herbaguideapps.data.local.dummyHistory

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<List<HistoryEntity>>().apply {
        value = dummyHistory
    }
    val text: LiveData<List<HistoryEntity>> = _text
}