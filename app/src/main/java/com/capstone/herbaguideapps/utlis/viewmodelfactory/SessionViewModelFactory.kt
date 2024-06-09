package com.capstone.herbaguideapps.utlis.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.herbaguideapps.data.repos.SessionRepository
import com.capstone.herbaguideapps.di.Injection
import com.capstone.herbaguideapps.session.SessionViewModel

class SessionViewModelFactory private constructor(
    private val sessionRepository: SessionRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SessionViewModel::class.java) -> {
                SessionViewModel(sessionRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SessionViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): SessionViewModelFactory {
            if (INSTANCE == null) {
                synchronized(SessionViewModelFactory::class.java) {
                    INSTANCE = SessionViewModelFactory(Injection.provideSessionRepository(context))
                }
            }
            return INSTANCE as SessionViewModelFactory
        }

    }
}