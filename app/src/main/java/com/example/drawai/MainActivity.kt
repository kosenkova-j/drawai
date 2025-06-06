package com.example.drawai

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.drawai.databinding.ActivityMainBinding
import com.example.drawai.generation.DrawingActivity
import com.example.drawai.generation.DrawingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: DrawingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupFab()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = ArtAdapter { art ->
            startActivity(Intent(this, DrawingActivity::class.java).apply {
                putExtra("art_id", art.id)
            })
        }
    }

    private fun setupFab() {
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, DrawingActivity::class.java))
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.savedArts.collect { arts ->
                (binding.recyclerView.adapter as? ArtAdapter)?.submitList(arts)
                binding.emptyView.visibility = if (arts.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }
}