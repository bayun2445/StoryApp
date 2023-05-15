package com.example.storyapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.SuccessResponse
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.ui.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener {
            register()
        }

    }

    private fun register() {
        val name = binding.edRegisterName.text.toString()
        val email = binding.edRegisterEmail.text.toString()
        val password = binding.edRegisterPassword.text.toString()

        val client = ApiConfig.getApiService().register(name, email, password)
        client.enqueue(object : Callback<SuccessResponse> {
            override fun onResponse(call: Call<SuccessResponse>, response: Response<SuccessResponse>) {
               if (response.body()?.error == false) {
                   Toast.makeText(
                       this@RegisterActivity,
                       "Register success",
                       Toast.LENGTH_LONG
                   ).show()

                   Intent(this@RegisterActivity, LoginActivity::class.java).also {
                       startActivity(it)
                   }
               } else {
                   Toast.makeText(
                       this@RegisterActivity,
                       "Register failed: ${response.body()?.message}",
                       Toast.LENGTH_LONG
                   ).show()
               }
            }

            override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                Toast.makeText(
                    this@RegisterActivity,
                    "Register failed: ${t.message.toString()}",
                    Toast.LENGTH_LONG
                ).show()
            }

        })
    }

    companion object {
        private val TAG = RegisterActivity::class.java.simpleName
    }
}