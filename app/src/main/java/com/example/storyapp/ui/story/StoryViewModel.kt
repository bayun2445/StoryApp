package com.example.storyapp.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.api.StoryItem
import com.example.storyapp.data.StoryRepository

class StoryViewModel(private val storyRepository: StoryRepository): ViewModel() {
    private val _listStory = MutableLiveData<List<StoryItem?>?>()
    private val _toastText = MutableLiveData<String?>()

    val listStory: LiveData<List<StoryItem?>?> = _listStory
    val toastText: LiveData<String?> = _toastText

    init {
        getPagesStories()
    }

    fun getPagesStories(): LiveData<PagingData<StoryItem>> {
        val pagingData = storyRepository.getPagesStory().cachedIn(viewModelScope)
        _toastText.value = pagingData.toString()
        return  pagingData
    }
    fun logout() {
        storyRepository.logout()
    }
}