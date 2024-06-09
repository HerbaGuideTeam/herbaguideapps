package com.capstone.herbaguideapps.utlis.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.herbaguideapps.data.repos.PredictRepository
import com.capstone.herbaguideapps.di.Injection
import com.capstone.herbaguideapps.ui.identify.PredictViewModel

class PredictViewModelFactory private constructor(
    private val predictRepository: PredictRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(PredictViewModel::class.java) -> {
                PredictViewModel(predictRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: PredictViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): PredictViewModelFactory {
            if (INSTANCE == null) {
                synchronized(PredictViewModelFactory::class.java) {
                    INSTANCE = PredictViewModelFactory(Injection.providePredictRepository(context))
                }
            }
            return INSTANCE as PredictViewModelFactory
        }

    }
}