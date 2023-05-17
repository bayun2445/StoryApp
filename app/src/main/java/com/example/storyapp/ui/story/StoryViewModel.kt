package com.example.storyapp.ui.story

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.ListStoryItem
import com.example.storyapp.api.StoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel(val sharedPref: SharedPreferences): ViewModel() {
    private val _listStory = MutableLiveData<List<ListStoryItem?>?>()
    private val _toastText = MutableLiveData<String?>()

    val listStory: LiveData<List<ListStoryItem?>?> = _listStory //TODO: Make the list loaded in story activity
    val toastText: LiveData<String?> = _toastText

    init {
        getAllStories()
    }
    fun getAllStories() {
        val savedToken = sharedPref.getString(TOKEN_KEY, null)
        val bearerToken = "Bearer $savedToken"

        val client = ApiConfig.getApiService().getAllStories(bearerToken)

        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                val responseBody = response.body()

                if (responseBody?.error == false) {

                    _listStory.value = responseBody.listStory
                    Log.d(TAG, responseBody.listStory.toString())
                    _toastText.value = "Load success: ${responseBody.message}"

                } else {
                    _toastText.value = "Load failed: ${responseBody?.message}"
                    Log.d(TAG, "Load failed: ${responseBody?.message}")
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                _toastText.value = "Load failed: ${t.message}"
                Log.d(TAG, "Load failed: ${t.message}")
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