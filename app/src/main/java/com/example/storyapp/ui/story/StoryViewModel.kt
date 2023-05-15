package com.example.storyapp.ui.story

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel

class StoryViewModel(val sharedPref: SharedPreferences): ViewModel() {
    fun logout() {
        sharedPref.edit()
            .remove(TOKEN_KEY)
            .apply()

        val check = sharedPref.getString(TOKEN_KEY, null)

        if (check.isNullOrEmpty()) {
            Log.d(
                TAG,
                "Account logged out"
            )
        }
    }

    companion object {
        private val TAG = StoryViewModel::class.java.simpleName
        private const val TOKEN_KEY = "login_token"
    }
}