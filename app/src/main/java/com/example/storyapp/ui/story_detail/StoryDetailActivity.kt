package com.example.storyapp.ui.story_detail

import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.storyapp.api.StoryItem
import com.example.storyapp.databinding.ActivityStoryDetailBinding
import java.util.Date
import java.util.Locale

class StoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryDetailBinding
    private var story: StoryItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.topBarMenu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        story = intent.getParcelableExtra("story")

        story?.let {
            loadStoryDetail(it)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    private fun loadStoryDetail(story: StoryItem) {
        Glide.with(this)
            .load(story.photoUrl)
            .into(binding.ivDetailPhoto)

        binding.apply {
            tvDetailDate.text = story.createdAt?.let { convertStringToDate(it) }
            tvDetailName.text = story.name
            tvDetailDescription.text = story.description
        }
    }

    private fun convertStringToDate(inputString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        val date: Date = inputFormat.parse(inputString)
        return outputFormat.format(date)
    }
}