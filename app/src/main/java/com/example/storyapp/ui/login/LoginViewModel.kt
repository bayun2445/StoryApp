package com.example.storyapp.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.LoginResponse
import com.example.storyapp.helper.LoginPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: LoginPreference): ViewModel() {
    private val _toastText = MutableLiveData<String?>()

    val toastText: LiveData<String?> = _toastText

    fun signIn(email: String, password: String) {
        val client = ApiConfig.getApiService().login(email, password)

        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                val responseBody = response.body()

                if (responseBody?.error == false) {
                    val token = responseBody.loginResult?.token
                    _toastText.value = "Login succeed"
                    Log.d(
                        TAG,
                        "name: ${responseBody.loginResult?.name}\n" +
                                "${responseBody.loginResult?.token}\n" +
                                "userId: ${responseBody.loginResult?.userId}"
                    )
                    storeAuthorizationToken(token)
                } else if (responseBody?.error == true){
                    _toastText.value = "Login Failed"
                    Log.d(
                        TAG,
                        "onResponse: Failed. ${responseBody.message}"
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
        token?.let {
            viewModelScope.launch {
                pref.setLoginToken(it)
            }
        }

        _toastText.value = "Token Stored"
    }

    companion object {
        private val TAG = LoginViewModel::class.java.simpleName
    }
}