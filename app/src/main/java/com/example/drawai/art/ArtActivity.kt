package com.example.drawai.art

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KProperty

@AndroidEntryPoint
class ArtActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArtDetailBinding
    private val viewModel: ArtViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        observeViewModel()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun observeViewModel() {
        // Получаем ID арта из intent
        val artId = intent.getIntExtra(EXTRA_ART_ID, -1)
        viewModel.loadArt(artId)

        viewModel.art.observe(this) { art ->
            art?.let {
                binding.art = it
                updateFavoriteButton(it.isFavorite)
            }
        }
    }

    private fun setupClickListeners() {
        binding.saveButton.setOnClickListener {
            viewModel.saveArtToGallery()
        }

        binding.shareButton.setOnClickListener {
            shareArtImage(binding.artImage.drawable)
        }

        binding.favButton.setOnClickListener {
            viewModel.toggleFavorite()
        }
    }

    private fun shareArtImage(drawable: Drawable?) {
        // Реализация шаринга изображения через Intent
    }

    companion object {
        const val EXTRA_ART_ID = "extra_art_id"

        fun newIntent(context: Context, artId: Int): Intent {
            return Intent(context, ArtActivity::class.java).apply {
                putExtra(EXTRA_ART_ID, artId)
            }
        }
    }
}
