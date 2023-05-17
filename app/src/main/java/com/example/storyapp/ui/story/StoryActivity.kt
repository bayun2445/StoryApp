package com.example.storyapp.ui.story

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.api.ListStoryItem
import com.example.storyapp.databinding.ActivityStoryBinding
import com.example.storyapp.helper.ViewModelFactory
import com.example.storyapp.ui.story_detail.StoryDetailActivity
import com.example.storyapp.ui.add_story.AddStoryActivity
import com.example.storyapp.ui.login.LoginActivity

class StoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryBinding
    private lateinit var viewModel: StoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.topBarMenu)

        val sharedPreferences = applicationContext.getSharedPreferences(SHARED_PREF_KEY, MODE_PRIVATE)

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(sharedPreferences)
        )[StoryViewModel::class.java]

        binding.rvStory.layoutManager = LinearLayoutManager(this)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.toastText.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }

        viewModel.listStory.observe(this) { list ->
            list?.let {
                if (it.isNotEmpty()) {
                    loadStoryData(it)
                }
            }

        }
    }

    private fun loadStoryData(list: List<ListStoryItem?>) {
        val adapter = StoryListAdapter(list)

        adapter.setClicked(object : StoryListAdapter.ItemCLicked {
            override fun click(position: Int) {
                val id = list[position]?.id

                Intent(this@StoryActivity, StoryDetailActivity::class.java).also {
                    it.putExtra("id", id)
                    startActivity(it)
                }
            }
        })

        binding.rvStory.apply {
            this.adapter = adapter
            setHasFixedSize(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
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

            else ->
                true
        }
    }

    companion object {
        private val TAG = StoryActivity::class.java.simpleName
        private const val SHARED_PREF_KEY = "story_app_prefs"
    }
}