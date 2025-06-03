package com.example.drawai

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.drawai.api.Resource
import com.example.drawai.databinding.ActivityDrawingBinding
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
        binding.DrawingView.apply {
            setColor(Color.BLACK)
            setBrushSize(8f)
        }
    }

    private fun setupButtons() {
        binding.btnClear.setOnClickListener {
            binding.DrawingView.clearCanvas()
        }

        binding.btnGenerate.setOnClickListener {
            viewModel.generateAIArt(binding.DrawingView.getBitmap())
        }
    }

    private fun observeViewModel() {
        viewModel.artGenerationState.observe(this) { state ->
            when (state) {
                is Resource.Loading -> showLoading()
                is Resource.Success -> showResult(state.data)
                is Resource.Error -> showError(state.message)
            }
        }
    }

    private fun showResult(bitmap: Bitmap?) {
        bitmap?.let {
            binding.generatedImageView.visibility = View.VISIBLE
            binding.generatedImageView.setImageBitmap(it)
        }
    }
}