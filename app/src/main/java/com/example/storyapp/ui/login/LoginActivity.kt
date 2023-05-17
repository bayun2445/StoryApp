package com.example.storyapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.helper.ViewModelFactory
import com.example.storyapp.ui.register.RegisterActivity
import com.example.storyapp.ui.story.StoryActivity


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = applicationContext.getSharedPreferences(SHARED_PREF_KEY, MODE_PRIVATE)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(sharedPreferences)
        )[LoginViewModel::class.java]

        setButtonsOnClickListener()

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.toastText.observe(this) {
            Toast.makeText(
                this@LoginActivity,
                it,
                Toast.LENGTH_SHORT
            ).show()
            Log.d(TAG, it)
        }

        viewModel.isSucceed.observe(this) {
            if (it) {
                Intent(this@LoginActivity, StoryActivity::class.java).also { intent ->
                    startActivity(intent)
                }

                finish()
            }
        }

        viewModel.isLoading.observe(this) {
            it?.let {
                if (it) {
                    binding.cvLoading.visibility = View.VISIBLE
                } else {
                    binding.cvLoading.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun setButtonsOnClickListener() {

        binding.btnRegister.setOnClickListener {
            Intent(this@LoginActivity, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnSignIn.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            viewModel.signIn(email, password)
        }
    }

    companion object {
        private val TAG = LoginActivity::class.java.simpleName
        private const val SHARED_PREF_KEY = "story_app_prefs"
    }
}