package com.example.storyapp.data

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp.api.ApiService
import com.example.storyapp.api.LoginResponse
import com.example.storyapp.api.StoryItem
import com.example.storyapp.api.StoryListResponse
import com.example.storyapp.api.SuccessResponse
import com.example.storyapp.database.StoryDatabase
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call

class StoryRepository(
    private val sharedPref: SharedPreferences,
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getPagesStories(): LiveData<PagingData<StoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            remoteMediator = StoryRemoteMediator(getToken(), storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getPageStory()
            }
        ).liveData
    }

    fun getStoryWithLocation(): Call<StoryListResponse> {
        return apiService.getStoriesWithLocation(getToken())
    }

    fun signIn(email: String, password: String): Call<LoginResponse> {
        return apiService.login(email, password)
    }

    fun register(name: String, email: String, password: String): Call<SuccessResponse> {
        return apiService.register(name, email, password)
    }

    fun addNewStory(
        multipart: MultipartBody.Part,
        descriptionBody: RequestBody,
        latBody: RequestBody?,
        lonBody: RequestBody?
    ): Call<SuccessResponse> {
        return apiService.addNewStory(getToken(), multipart, descriptionBody, latBody, lonBody)
    }

    fun storeAuthorizationToken(token: String) {
        sharedPref.edit()
            .putString(TOKEN_KEY, token)
            .apply()
    }

    private fun getToken(): String {
        return "Bearer ${sharedPref.getString(TOKEN_KEY, "")}"
    }

    fun logout() {
        sharedPref.edit()
            .remove(TOKEN_KEY)
            .apply()
    }


    companion object {
        private const val TOKEN_KEY = "login_token"
    }
}