package com.example.storyapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.storyapp.databinding.ActivityStoryBinding

class StoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val token = intent.getStringExtra("token") ?: "Blank"
        Toast.makeText(this, token, Toast.LENGTH_LONG).show()
    }
}