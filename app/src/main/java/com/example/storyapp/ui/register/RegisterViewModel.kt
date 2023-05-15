package com.example.storyapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.SuccessResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(): ViewModel() {

    private val _toastText = MutableLiveData<String>()
    private val _isSucceed = MutableLiveData<Boolean>()

    val toastText: LiveData<String> = _toastText
    val isSucceed: LiveData<Boolean> = _isSucceed

    fun register(name: String, email: String, password: String) {
        val client = ApiConfig.getApiService().register(name, email, password)
        client.enqueue(object : Callback<SuccessResponse> {
            override fun onResponse(call: Call<SuccessResponse>, response: Response<SuccessResponse>) {
                if (response.body()?.error == false) {
                    _toastText.value = "Register success"
                    _isSucceed.value = true
                } else {
                    _toastText.value = "Register failed: ${response.body()?.message}"
                }
            }

            override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                _toastText.value =   "Register failed: ${t.message.toString()}"
            }
        })
    }
}