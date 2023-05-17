package com.example.storyapp.ui.story

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.StoryItem
import com.example.storyapp.api.StoryListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel(val sharedPref: SharedPreferences): ViewModel() {
    private val _listStory = MutableLiveData<List<StoryItem?>?>()
    private val _toastText = MutableLiveData<String?>()

    val listStory: LiveData<List<StoryItem?>?> = _listStory
    val toastText: LiveData<String?> = _toastText

    init {
        getAllStories()
    }
    fun getAllStories() {
        val savedToken = sharedPref.getString(TOKEN_KEY, null)
        val bearerToken = "Bearer $savedToken"

        val client = ApiConfig.getApiService().getAllStories(bearerToken)

        client.enqueue(object : Callback<StoryListResponse> {
            override fun onResponse(call: Call<StoryListResponse>, response: Response<StoryListResponse>) {
                val responseBody = response.body()

                if (responseBody?.error == false) {
                    _listStory.value = responseBody.listStory
                } else {
                    _toastText.value = "Load failed: ${responseBody?.message}"
                }
            }

            override fun onFailure(call: Call<StoryListResponse>, t: Throwable) {
                _toastText.value = "Load failed: ${t.message}"
            }

        })

    }
    fun logout() {
        sharedPref.edit()
            .remove(TOKEN_KEY)
            .apply()

        val check = sharedPref.getString(TOKEN_KEY, null)

        if (check.isNullOrEmpty()) {
            _toastText.value = "Account logged out"
        }
    }

    companion object {
        private const val TOKEN_KEY = "login_token"
    }
}