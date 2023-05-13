package com.example.storyapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.helper.ViewModelFactory
import com.example.storyapp.ui.RegisterActivity
import com.example.storyapp.ui.StoryActivity


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

        viewModel.toastText.observe(this) {
            it?.let {
                showToast(it)
                Log.d(TAG, it)
            }
        }
    }

    private fun setButtonsOnClickListener() {

        // register
        binding.btnRegister.setOnClickListener {
            Intent(this@LoginActivity, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }

        // sign in
        binding.btnSignIn.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            viewModel.signIn(email, password)

            Intent(this@LoginActivity, StoryActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun showToast(text: String?) {
        Toast.makeText(
            this@LoginActivity,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        private val TAG = LoginActivity::class.java.simpleName
        private const val SHARED_PREF_KEY = "story_app_prefs"
    }
}