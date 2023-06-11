package com.example.storyapp.di

import android.content.Context
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.database.StoryDatabase

object Injection {

    fun provideRepository(context: Context): StoryRepository {
        val sharedPreferences =
            context.getSharedPreferences("story_app_prefs", Context.MODE_PRIVATE)
        val apiService = ApiConfig.getApiService()
        val storyDatabase = StoryDatabase.getInstance(context)

        return StoryRepository.getInstance(sharedPreferences, storyDatabase, apiService)
    }
}