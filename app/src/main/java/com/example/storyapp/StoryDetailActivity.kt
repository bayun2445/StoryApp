package com.example.storyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.storyapp.databinding.ActivityStoryDetailBinding

class StoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}