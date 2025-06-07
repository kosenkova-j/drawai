package com.example.drawai

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.drawai.gallery.GalleryViewModel
import com.example.drawai.art.ArtActivity
import com.example.drawai.common.OnArtItemClickListener
import com.example.drawai.databinding.ActivityMainBinding
import com.example.drawai.domain.Art
import com.example.drawai.domain.ArtAdapter
import com.example.drawai.generation.GenerationActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnArtItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: GalleryViewModel by viewModels()
    private lateinit var artAdapter: ArtAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupObservers()
        setupListeners()
    }

    private fun setupRecyclerView() {
        artAdapter = ArtAdapter(
            onItemClick = this,
            onDeleteClick = object : OnArtItemClickListener {
                override fun onClick(art: Art) {
                    viewModel.deleteArt(art)
                }
            }
        )

        binding.artsRecyclerView.apply {
            adapter = artAdapter
            layoutManager = GridLayoutManager(
                this@MainActivity,
                2 // Фиксированное количество колонок
            )
            setHasFixedSize(true)
        }
    }

    private fun setupObservers() {
        viewModel.arts.observe(this) { arts ->
            artAdapter.submitList(arts)
        }
    }

    private fun setupListeners() {
        binding.fabCreateArt.setOnClickListener {
            startActivity(Intent(this, GenerationActivity::class.java))
        }
    }

    override fun onClick(art: Art) {
        startActivity(ArtActivity.newIntent(this, art.id))
    }
}