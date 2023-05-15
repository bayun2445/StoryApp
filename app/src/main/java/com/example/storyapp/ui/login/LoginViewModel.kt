package com.example.storyapp.ui.login

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(val sharedPref: SharedPreferences): ViewModel() {
    private val _toastText = MutableLiveData<String>()
    private val _isSucceed = MutableLiveData<Boolean>()

    val toastText: LiveData<String> = _toastText
    val isSucceed: LiveData<Boolean> = _isSucceed

    fun signIn(email: String, password: String) {
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
                    Log.d(
                        TAG,
                        "name: ${response.body()?.loginResult?.name}\n" +
                                "${response.body()?.loginResult?.token}\n" +
                                "userId: ${response.body()?.loginResult?.userId}"
                    )
                    storeAuthorizationToken(token)

                    _isSucceed.value = true
                } else {
                    _toastText.value = "Login Failed: ${response.body()?.message}"
                    Log.d(
                        TAG,
                        "onResponse: Failed. ${response.body()?.message}"
                    )
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _toastText.value = "Login Failed"
                Log.d(
                    TAG,
                    "onFailure: ${t.message.toString()}"
                )
            }

        })
    }

    private fun storeAuthorizationToken(token: String?) {
        sharedPref.edit()
            .putString(TOKEN_KEY, token)
            .apply()

        _toastText.value = "Token Stored"
    }

    companion object {
        private val TAG = LoginViewModel::class.java.simpleName
        private const val TOKEN_KEY = "login_token"
    }
}