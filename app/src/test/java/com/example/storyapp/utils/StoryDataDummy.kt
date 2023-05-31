package com.example.storyapp.utils

import com.example.storyapp.api.StoryItem

object StoryDataDummy {
    fun generateDummyStories() : List<StoryItem> {
        val newList = ArrayList<StoryItem>()
        for (i in 0..10) {
            val stories = StoryItem(
                photoUrl = "photo_url ",
                name = "Story $i",
                description = "Description $i",
                lon = 1.0,
                id = "id $i",
                lat = 2.0
            )
            newList.add(stories)
        }
        return newList
    }
}