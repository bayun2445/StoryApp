package com.example.storyapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.example.storyapp.api.StoryItem
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.ui.story.StoryViewModel
import com.example.storyapp.utils.MainDispatcherRule
import com.example.storyapp.utils.StoryDataDummy
import com.example.storyapp.utils.getOrAwaitValue
import com.example.storyapp.utils.toList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var storyViewModel: StoryViewModel
    private val storiesDummy = StoryDataDummy.generateDummyStories()


    @Before
    fun setUp() {
        storyViewModel = StoryViewModel(storyRepository)
    }

    @Test
    fun `when Get Pages Stories Succeed and Should Not Return Null`() = runTest {
        val expectedData: PagingData<StoryItem> = PagingData.from(storiesDummy)
        val expectedResult = MutableLiveData<PagingData<StoryItem>>()
        expectedResult.value = expectedData

        `when` (storyRepository.getPagesStories()).thenReturn(expectedResult as LiveData<PagingData<StoryItem>>)

        val actualStory = storyViewModel.getPagesStories().getOrAwaitValue()

        Mockito.verify(storyRepository).getPagesStories()

        Assert.assertNotNull(actualStory)

        val expectedResultList = expectedData.toList()
        val actualResultList = actualStory.toList()

        Assert.assertEquals(expectedResultList, actualResultList)
        Assert.assertEquals(expectedResultList.size, actualResultList.size)
        Assert.assertEquals(expectedResultList.first(), actualResultList.first())
    }

    @Test
    fun `when Get Pages Empty Should Not Return Data`() = runTest {
        val data: PagingData<StoryItem> = PagingData.empty()
        val expectedResult = MutableLiveData<PagingData<StoryItem>>()
        expectedResult.value = data

        `when` (storyRepository.getPagesStories()).thenReturn(expectedResult as LiveData<PagingData<StoryItem>>)

        val actualStory = storyViewModel.getPagesStories().getOrAwaitValue()

        Mockito.verify(storyRepository).getPagesStories()

        val actualResultList = actualStory.toList()

        Assert.assertEquals(0, actualResultList.size)
    }
}