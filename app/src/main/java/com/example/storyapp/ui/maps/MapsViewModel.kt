package com.example.storyapp.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.api.StoryItem
import com.example.storyapp.api.StoryListResponse
import com.example.storyapp.data.StoryRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    private val _listLocationStory = MutableLiveData<List<StoryItem>>()

    val listLocationStory: LiveData<List<StoryItem>> = _listLocationStory

    init {
        getStoryWithLocation()
    }

    private fun getStoryWithLocation() {
        storyRepository.getStoryWithLocation()
            .enqueue(object : Callback<StoryListResponse> {
                override fun onResponse(
                    call: Call<StoryListResponse>,
                    response: Response<StoryListResponse>
                ) {
                    val responseBody = response.body()
                    if (response.isSuccessful) {
                        _listLocationStory.value = responseBody?.listStory
                        Timber.d(responseBody?.listStory.toString())
                    } else {
                        Timber.e("Failed: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<StoryListResponse>, t: Throwable) {
                    Timber.e("Failed: ${t.message}")
                }

            })
    }
}