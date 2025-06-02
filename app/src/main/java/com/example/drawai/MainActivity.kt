package com.example.drawai

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGalleryBinding
    private val viewModel: DrawingViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupFab()
    }

    private fun observeViewModel() {
        viewModel.savedArts.observe(this) { arts ->
            (binding.recyclerView.adapter as ArtAdapter).submitList(arts)
            binding.emptyView.visibility = if (arts.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(this@DrawingActivity, 2)
            adapter = ArtAdapter { art ->
                // Показываем детали арта
                startActivity(Intent(this@DrawingActivity, ArtDetailActivity::class.java).apply {
                    putExtra("art_id", art.id)
                })
            }
        }
    }

    private fun setupFab() {
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, DrawingActivity::class.java))
        }
    }
}