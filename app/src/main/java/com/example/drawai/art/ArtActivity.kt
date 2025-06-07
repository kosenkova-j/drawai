package com.example.drawai.art

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.drawai.databinding.ArtActivityDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArtActivity : AppCompatActivity() {

    private lateinit var binding: ArtActivityDetailBinding
    private val viewModel: ArtViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ArtActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.activity = this

        binding.saveButton.setOnClickListener {
            viewModel.saveArtToGallery()
        }

        viewModel.toastMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        setupToolbar()
        observeViewModel()
        setupClickListeners()
    }

    fun onNavigationClick() {
        finish()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun observeViewModel() {
        val artId = intent.getIntExtra(EXTRA_ART_ID, -1)
        viewModel.loadArt(artId)
    }

    private fun setupClickListeners() {
        binding.saveButton.setOnClickListener {
            viewModel.saveArtToGallery()
            Toast.makeText(this, "Art saved", Toast.LENGTH_SHORT).show()
        }
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