package com.example.storyapp.ui.story

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.BuildConfig
import com.example.storyapp.R
import com.example.storyapp.api.StoryItem
import com.example.storyapp.databinding.ActivityStoryBinding
import com.example.storyapp.helper.ViewModelFactory
import com.example.storyapp.ui.add_story.AddStoryActivity
import com.example.storyapp.ui.login.LoginActivity
import com.example.storyapp.ui.maps.MapsActivity
import timber.log.Timber

class StoryActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityStoryBinding.inflate(layoutInflater)
    }
    private val viewModel: StoryViewModel by viewModels {
        ViewModelFactory(this)
    }

    private lateinit var adapter: StoryPagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.topBarMenu)

        adapter = StoryPagingAdapter()
        binding.rvStory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false).also {
                it.isSmoothScrollbarEnabled = true
                it.stackFromEnd = false
            }

        viewModel.getPagesStories()
        observeViewModel()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume: ")
        viewModel.getPagesStories()
        adapter.refresh()
    }

    private fun observeViewModel() {
        viewModel.getPagesStories().observe(this) {
            loadStoryData(it)
        }

        viewModel.toastText.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
    }

    private fun loadStoryData(pagingData: PagingData<StoryItem>) {
        adapter.submitData(lifecycle, pagingData)

        binding.rvStory.adapter = adapter
        binding.rvStory.setHasFixedSize(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_logout -> {
                viewModel.logout()

                Intent(this, LoginActivity::class.java).also {
                    startActivity(it)
                }

                finish()
                true
            }

            R.id.menu_add -> {
                Intent(this, AddStoryActivity::class.java).also {
                    startActivity(it)
                }

                true
            }

            R.id.menu_maps -> {
                Intent(this, MapsActivity::class.java).also {
                    startActivity(it)
                }

                true
            }

            else ->
                true
        }
    }
}