package com.example.storyapp.helper

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoginPreference private constructor(private val dataStore: DataStore<Preferences>) {
    private val TOKEN_KEY = stringPreferencesKey("login_token")

    fun getLoginToken(): Flow<String> {
        return dataStore.data.map {
            it[TOKEN_KEY].toString()
        }
    }

    suspend fun setLoginToken(token: String)  {
        dataStore.edit {
            it[TOKEN_KEY] = token
        }
    }

    companion object {
        @Volatile
        private var instance: LoginPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): LoginPreference {
            return instance ?: synchronized(this) {
                val pref = LoginPreference(dataStore)
                this.instance = pref
                pref
            }
        }
    }
}