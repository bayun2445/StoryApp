package com.example.storyapp.ui.add_story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.api.SuccessResponse
import com.example.storyapp.data.StoryRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class AddStoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    private val _toastText = MutableLiveData<String>()
    private val _isSucceed = MutableLiveData<Boolean>()
    private val _isLoading = MutableLiveData<Boolean>()

    val toastText: LiveData<String> = _toastText
    val isSucceed: LiveData<Boolean> = _isSucceed
    val isLoading: LiveData<Boolean> = _isLoading

    fun uploadStory(imageFile: File, description: String, lat: Float?, lon: Float?) {
        _isLoading.value = true
        val requestLatitude = lat?.toString()?.toRequestBody("text/plain".toMediaType())
        val requestLongitude = lon?.toString()?.toRequestBody("text/plain".toMediaType())
        val requestDescription = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val imageMultipart = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )

        storyRepository.addNewStory(
            imageMultipart,
            requestDescription,
            requestLatitude,
            requestLongitude
        ).enqueue(object : Callback<SuccessResponse> {
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