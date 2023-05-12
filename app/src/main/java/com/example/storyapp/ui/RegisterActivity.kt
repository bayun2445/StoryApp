package com.example.storyapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.StoryResponse
import com.example.storyapp.databinding.ActivityRegisterBinding
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
        val name = binding.edRegisterUsername.text.toString()
        val email = binding.edRegisterEmail.text.toString()
        val password = binding.edRegisterPassword.text.toString()

        val client = ApiConfig.getApiService().register(name, email, password)
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                Toast.makeText(
                    this@RegisterActivity,
                    "Registrasi Berhasil",
                    Toast.LENGTH_LONG
                ).show()

                Intent(this@RegisterActivity, LoginActivity::class.java).also {
                    startActivity(it)
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Toast.makeText(
                    this@RegisterActivity,
                    "Registrasi Gagal: ${t.message.toString()}",
                    Toast.LENGTH_LONG
                ).show()
            }

        })
    }

    companion object {
        private val TAG = RegisterActivity::class.java.simpleName
    }
}