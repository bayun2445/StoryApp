package com.example.storyapp.ui.register

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.helper.ViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    private val viewModel: RegisterViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        seButtonsOnClickListener()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.toastText.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }

        viewModel.isSucceed.observe(this) {
            if (it) {
                finish()
            }
        }

        viewModel.isLoading.observe(this) {
            it?.let {
                binding.cvLoading.isVisible = it
            }
        }
    }

    private fun seButtonsOnClickListener() {
        binding.btnSignUp.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            viewModel.register(name, email, password)
        }
    }
}