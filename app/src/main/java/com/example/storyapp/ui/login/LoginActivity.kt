package com.example.storyapp.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.helper.LoginPreference
import com.example.storyapp.helper.ViewModelFactory
import com.example.storyapp.ui.RegisterActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "prefs")

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = LoginPreference.getInstance(dataStore)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(preferences)
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
    }
}