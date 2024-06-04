package com.capstone.herbaguideapps.ui.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.capstone.herbaguideapps.data.remote.response.ArticlesItem
import com.capstone.herbaguideapps.data.repos.ExploreRepository

class ExploreViewModel(private val exploreRepository: ExploreRepository) : ViewModel() {

    val listExplore: LiveData<PagingData<ArticlesItem>> =
        exploreRepository.getHeadlineNews().cachedIn(viewModelScope)

}