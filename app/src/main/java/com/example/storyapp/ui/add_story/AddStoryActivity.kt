package com.example.storyapp.ui.add_story

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityAddStoryBinding
import com.example.storyapp.helper.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityAddStoryBinding.inflate(layoutInflater)
    }
    private val viewModel: AddStoryViewModel by viewModels {
        ViewModelFactory(this)
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var longitude: Float? = null
    private var latitude: Float? = null
    private var imageFile: File? = null

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as Bitmap
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

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }

                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }

                else -> {
                    showToast("Access to Location Failed")
                    binding.checkBoxLocation.isChecked = false
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.topBarMenu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setButtonsClickListener()
        observeViewModel()

        binding.checkBoxLocation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getMyLastLocation()
            } else {
                longitude = null
                latitude = null
            }
        }

    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if (it != null) {
                    longitude = it.longitude.toFloat()
                    latitude = it.latitude.toFloat()
                } else {
                    showToast("Location not found")
                    binding.checkBoxLocation.isChecked = false
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
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
                    viewModel.uploadStory(it, description, latitude, longitude)
                }
            } else {
                showToast("All fields must be filled")
            }
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}