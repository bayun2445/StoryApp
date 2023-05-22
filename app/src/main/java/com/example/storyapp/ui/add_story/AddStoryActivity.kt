package com.example.storyapp.ui.add_story

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityAddStoryBinding
import com.example.storyapp.helper.ViewModelFactory
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var viewModel: AddStoryViewModel

    private var imageFile: File? = null

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val imageBitmap = it.data?.extras?.get("data") as Bitmap
            imageFile = bitmapToFile(imageBitmap, this)
            binding.ivPhotoPreview.setImageBitmap(imageBitmap)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                imageFile = uriToFile(selectedImg, this)
                binding.ivPhotoPreview.setImageURI(uri)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.topBarMenu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sharedPref = applicationContext.getSharedPreferences(SHARED_PREF_KEY, MODE_PRIVATE)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(sharedPref)
        )[AddStoryViewModel::class.java]

        setButtonsClickListener()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.toastText.observe(this) {
            showToast(it)
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

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setButtonsClickListener() {
        binding.btnCamera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            launcherIntentCamera.launch(intent)
        }

        binding.btnGallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"

            val chooser = Intent.createChooser(intent, getString(R.string.chooser))

            launcherIntentGallery.launch(chooser)
        }

        binding.btnUpload.setOnClickListener {
            val description = binding.edAddDescription.text.toString()
            if (imageFile != null && description.isNotEmpty()) {
                imageFile?.let {
                    viewModel.uploadStory(it, description)
                }
            } else {
                showToast("All fields must be filled")
            }
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val SHARED_PREF_KEY = "story_app_prefs"
    }
}