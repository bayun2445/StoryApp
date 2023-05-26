package com.example.storyapp.ui.story_detail

import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.api.StoryItem
import com.example.storyapp.databinding.ActivityStoryDetailBinding
import com.example.storyapp.maps.MapsActivity
import java.util.Date
import java.util.Locale

class StoryDetailActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityStoryDetailBinding.inflate(layoutInflater)
    }
    private var story: StoryItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.topBarMenu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        story = intent.getParcelableExtra(STORY_EXTRA)

        story?.let {
            loadStoryDetail(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_maps -> {
                Intent(this@StoryDetailActivity, MapsActivity::class.java).also {
                    it.putExtra(STORY_EXTRA, story)
                    startActivity(it)
                }

                true
            }

            else -> {
                true
            }
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

    companion object {
        private const val STORY_EXTRA = "story"
    }
}