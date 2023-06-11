package com.example.storyapp.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.api.StoryItem
import com.example.storyapp.data.StoryRepository

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    private val _toastText = MutableLiveData<String?>()

    val toastText: LiveData<String?> = _toastText

    fun getPagesStories(): LiveData<PagingData<StoryItem>> {
        return storyRepository.getPagesStories().cachedIn(viewModelScope)
    }

    fun logout() {
        storyRepository.logout()
    }
}