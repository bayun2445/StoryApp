package com.example.storyapp.data

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp.api.ApiService
import com.example.storyapp.api.StoryItem
import com.example.storyapp.database.StoryDatabase

class StoryRepository(
    private val sharedPref: SharedPreferences,
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService
    ) {
    @OptIn(ExperimentalPagingApi::class)
    fun getPagesStory(): LiveData<PagingData<StoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(getToken(), storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getPageStory()
            }
        ).liveData
    }

    fun storeAuthorizationToken(token: String) {
        sharedPref.edit()
            .putString(TOKEN_KEY, token)
            .apply()
    }

    fun getToken(): String {
        return sharedPref.getString(TOKEN_KEY, "") as String
    }

    fun logout() {
        sharedPref.edit()
            .remove(TOKEN_KEY)
            .apply()
    }

    companion object{
        private const val TOKEN_KEY = "login_token"
    }
}