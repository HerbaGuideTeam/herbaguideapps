package com.capstone.herbaguideapps.utlis

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.herbaguideapps.data.repos.ExploreRepository
import com.capstone.herbaguideapps.di.Injection
import com.capstone.herbaguideapps.ui.explore.ExploreViewModel

class ViewModelFactory private constructor(private val exploreRepository: ExploreRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExploreViewModel::class.java)) {
            return ExploreViewModel(exploreRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideExploreRepository(context))
            }.also { instance = it }
    }
}