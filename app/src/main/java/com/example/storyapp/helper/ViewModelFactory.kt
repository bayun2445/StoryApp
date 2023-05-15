package com.example.storyapp.helper

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.ui.login.LoginViewModel
import com.example.storyapp.ui.story.StoryViewModel

class ViewModelFactory(private val mSharedPref: SharedPreferences?): ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(sharedPref: SharedPreferences?): ViewModelFactory {
            return instance ?: synchronized(this) {
                val factory = ViewModelFactory(sharedPref)
                instance = factory
                instance as ViewModelFactory
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(sharedPref = mSharedPref!!) as T
        } else if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            return StoryViewModel(sharedPref = mSharedPref!!) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class ${modelClass.name}")
    }
}