package com.example.storyapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapp.api.ApiService
import com.example.storyapp.api.StoryItem
import com.example.storyapp.api.StoryListResponse
import timber.log.Timber

class StoryPagingSource(
    private val apiService: ApiService,
    private val authToken: String
): PagingSource<Int, StoryItem>() {
    override fun getRefreshKey(state: PagingState<Int, StoryItem>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryItem> {
        try {
            val position = params.key ?: INITIAL_INDEX

            Timber.d("Paging API Request - Page: $position")
            val responseData: StoryListResponse = apiService.getPagesStories(
                authToken = "Bearer $authToken",
                page = position,
                size = params.loadSize
            )


            Timber.d("Paging API Response - Page: $position, Data: $responseData")

            val responseList: List<StoryItem> = responseData.listStory

            return LoadResult.Page(
                data = responseList,
                prevKey = if (position == INITIAL_INDEX) null else position - 1,
                nextKey = if (responseList.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    companion object {
        private const val INITIAL_INDEX = 1
    }
}