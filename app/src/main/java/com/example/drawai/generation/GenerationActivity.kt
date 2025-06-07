package com.example.drawai.generation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.drawai.presentation.gallery.GalleryActivity
import com.example.drawai.databinding.ActivityArtGenerationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GenerationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArtGenerationBinding
    private val viewModel: GenerationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtGenerationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        //setupToolbar()
        setupListeners()
        observeViewModel()
    }

//    private fun setupToolbar() {
//        setSupportActionBar(binding.toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        binding.toolbar.setNavigationOnClickListener { finish() }
//    }

    private fun setupListeners() {
        binding.generateButton.setOnClickListener {
            viewModel.generateArt()
        }

        binding.saveButton.setOnClickListener {
            viewModel.saveArt()
            finish() // Закрываем после сохранения
        }
    }

    private fun observeViewModel() {
        viewModel.navigateToGallery.observe(this) { event ->
            event.getContentIfNotHandled()?.let { shouldNavigate ->
                if (shouldNavigate) {
                    startActivity(Intent(this, GalleryActivity::class.java))
                    finish()
                }
            }
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, GenerationActivity::class.java)
        }
    }
}