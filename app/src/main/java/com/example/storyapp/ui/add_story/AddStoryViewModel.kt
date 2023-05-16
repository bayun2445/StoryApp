package com.example.storyapp.ui.add_story

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.SuccessResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import java.io.File
import retrofit2.Callback
import retrofit2.Response


class AddStoryViewModel(val sharedPref: SharedPreferences): ViewModel() {
    private val _toastText = MutableLiveData<String>()
    private val _isSucceed = MutableLiveData<Boolean>()

    val toastText: LiveData<String> = _toastText
    val isSucceed: LiveData<Boolean> = _isSucceed

    fun uploadStory(imageFile: File, description: String) {
        val savedToken = sharedPref.getString(TOKEN_KEY, null)
        val bearerToken = "Bearer $savedToken"

        val requestDescription = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val imageMultipart = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )

        val client = ApiConfig.getApiService().addNewStory(bearerToken, imageMultipart, requestDescription)
        client.enqueue(object : Callback<SuccessResponse> {
            override fun onResponse(
                call: Call<SuccessResponse>,
                response: Response<SuccessResponse>
            ) {
                val responseBody = response.body()
                if (responseBody?.error == false) {
                    _toastText.value = "Story successfully added"
                    _isSucceed.value = true
                } else {
                    _toastText.value = "Failed: ${responseBody?.message}"
                    Log.e(TAG, "Failed: ${responseBody?.message}")
                }
            }

            override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                Log.e(TAG, "Failed: ${t.message}")
            }
        })
    }

    companion object {
        private val TAG = AddStoryViewModel::class.java.simpleName
        private const val TOKEN_KEY = "login_token"
    }
}