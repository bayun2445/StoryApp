package com.example.storyapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.api.SuccessResponse
import com.example.storyapp.data.StoryRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val storyRepository: StoryRepository): ViewModel() {

    private val _toastText = MutableLiveData<String>()
    private val _isSucceed = MutableLiveData<Boolean>()
    private val _isLoading = MutableLiveData<Boolean>()

    val toastText: LiveData<String> = _toastText
    val isSucceed: LiveData<Boolean> = _isSucceed
    val isLoading: LiveData<Boolean> = _isLoading

    fun register(name: String, email: String, password: String) {
        _isLoading.value = true
        storyRepository.register(name, email, password)
            .enqueue(object : Callback<SuccessResponse> {
                override fun onResponse(call: Call<SuccessResponse>, response: Response<SuccessResponse>) {
                    if (response.body()?.error == false) {
                        _toastText.value = "Register success"
                        _isSucceed.value = true
                        _isLoading.value = false
                    } else {
                        _toastText.value = "Register failed: ${response.message()}"
                        _isLoading.value = false
                    }
                }

                override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                    _toastText.value =   "Register failed: ${t.message.toString()}"
                    _isLoading.value = false
                }
            })
    }
}