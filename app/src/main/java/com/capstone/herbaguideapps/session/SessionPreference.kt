package com.capstone.herbaguideapps.session


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class SessionPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    fun getToken(): Flow<String> {
        return dataStore.data.map { preference: Preferences ->
            preference[TOKEN_KEY] ?: ""
        }
    }

    fun getSession(): Flow<SessionModel> {
        return dataStore.data.map { preference: Preferences ->
            SessionModel(
                preference[NAME_KEY] ?: "",
                preference[EMAIL_KEY] ?: "",
                preference[STATE_KEY] ?: false,
                preference[TOKEN_KEY] ?: "",
                preference[GUEST_KEY] ?: false
            )
        }
    }

    suspend fun saveSession(session: SessionModel) {
        dataStore.edit { preference ->
            preference[NAME_KEY] = session.name
            preference[EMAIL_KEY] = session.email
            preference[STATE_KEY] = session.isLogin
            preference[TOKEN_KEY] = session.token
            preference[GUEST_KEY] = session.isGuest
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SessionPreferences? = null

        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val STATE_KEY = booleanPreferencesKey("state")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val GUEST_KEY = booleanPreferencesKey("guest")

        fun getInstance(dataStore: DataStore<Preferences>): SessionPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SessionPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}