package com.example.storyapp.ui.add_story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.SuccessResponse
import com.example.storyapp.data.StoryRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import java.io.File
import retrofit2.Callback
import retrofit2.Response


class AddStoryViewModel(private val storyRepository: StoryRepository): ViewModel() {
    private val _toastText = MutableLiveData<String>()
    private val _isSucceed = MutableLiveData<Boolean>()
    private val _isLoading = MutableLiveData<Boolean>()

    val toastText: LiveData<String> = _toastText
    val isSucceed: LiveData<Boolean> = _isSucceed
    val isLoading: LiveData<Boolean> = _isLoading

    fun uploadStory(imageFile: File, description: String, lat: Float?, lon: Float?) {
        _isLoading.value = true
        val savedToken = storyRepository.getToken()
        val bearerToken = "Bearer $savedToken"

        val requestLatitude = lat?.toString()?.toRequestBody("text/plain".toMediaType())
        val requestLongitude = lon?.toString()?.toRequestBody("text/plain".toMediaType())
        val requestDescription = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val imageMultipart = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )

        val client = ApiConfig.getApiService().addNewStory(
            bearerToken,
            imageMultipart,
            requestDescription,
            requestLatitude,
            requestLongitude
        )

        client.enqueue(object : Callback<SuccessResponse> {
            override fun onResponse(
                call: Call<SuccessResponse>,
                response: Response<SuccessResponse>
            ) {
                val responseBody = response.body()
                if (responseBody?.error == false) {
                    _toastText.value = "Story successfully added"
                    _isSucceed.value = true
                    _isLoading.value = false
                } else {
                    _toastText.value = "Failed: ${response.message()}"
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                _toastText.value = "Failed: ${t.message}"
                _isLoading.value = false
            }
        })
    }
}