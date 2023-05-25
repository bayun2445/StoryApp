package com.example.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.LoginResponse
import com.example.storyapp.data.StoryRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val storyRepository: StoryRepository): ViewModel() {
    private val _toastText = MutableLiveData<String>()
    private val _isSucceed = MutableLiveData<Boolean>()
    private val _isLoading = MutableLiveData<Boolean>()

    val toastText: LiveData<String> = _toastText
    val isSucceed: LiveData<Boolean> = _isSucceed
    val isLoading: LiveData<Boolean> = _isLoading

    fun signIn(email: String, password: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                response.body()
                if (response.body()?.error == false) {
                    val token = response.body()?.loginResult?.token
                    _toastText.value = "Login succeed"

                    _isLoading.value = false
                    _isSucceed.value = true

                    storeAuthorizationToken(token)
                } else {
                    _isLoading.value = false
                    _toastText.value = "Login Failed: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _toastText.value = "Login Failed: ${t.message}"
            }

        })
    }

    private fun storeAuthorizationToken(token: String?) {
        token?.let {
            storyRepository.storeAuthorizationToken(it)
        }
    }
}