package com.example.storyapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.isVisible
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.helper.ViewModelFactory
import com.example.storyapp.ui.register.RegisterActivity
import com.example.storyapp.ui.story.StoryActivity


class LoginActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private val viewModel: LoginViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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
                binding.cvLoading.isVisible = it
            }
        }
    }

    private fun setButtonsOnClickListener() {

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)

            val optionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(
                    this,
                    Pair(binding.edLoginEmail, "email"),
                    Pair(binding.edLoginPassword, "password")
                )

            startActivity(intent, optionsCompat.toBundle())
        }

        binding.btnSignIn.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            viewModel.signIn(email, password)
        }
    }
}