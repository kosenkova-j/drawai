package com.example.drawai

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.load.engine.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DrawingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDrawingBinding
    private val viewModel: DrawingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDrawingView()
        setupButtons()
        observeViewModel()
    }

    private fun setupDrawingView() {
        binding.drawingView.apply {
            setColor(Color.BLACK)
            setBrushSize(8f)
        }
    }

    private fun setupButtons() {
        binding.btnGenerate.setOnClickListener {
            val bitmap = binding.drawingView.getBitmap()
            viewModel.generateAIArt(bitmap)
        }

        binding.btnClear.setOnClickListener {
            binding.drawingView.clearCanvas()
        }
    }

    private fun observeViewModel() {
        viewModel.artGenerationState.observe(this) { state ->
            when (state) {
                is Resource.Loading -> showLoading()
                is Resource.Success -> {
                    hideLoading()
                    state.data?.let { showGeneratedArt(it) }
                }
                is Resource.Error -> {
                    hideLoading()
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showGeneratedArt(bitmap: Bitmap) {
        binding.generatedImageView.visibility = View.VISIBLE
        binding.generatedImageView.setImageBitmap(bitmap)
        binding.btnSave.visibility = View.VISIBLE

        binding.btnSave.setOnClickListener {
            viewModel.saveArt(
                original = binding.drawingView.getBitmap(),
                generated = bitmap
            )
            finish()
        }
    }
}