package com.drawai.presentation.gallery

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.drawai.databinding.GalleryActivityBinding
import com.drawai.gallery.GalleryViewModel
import com.example.drawai.R
import com.example.drawai.art.ArtActivity
import com.example.drawai.generation.GenerationActivity
import com.example.drawai.common.OnArtItemClickListener
import com.example.drawai.domain.Art
import com.example.drawai.domain.ArtAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryActivity : AppCompatActivity(), OnArtItemClickListener {

    private lateinit var binding: GalleryActivityBinding
    private val viewModel: GalleryViewModel by viewModels()
    private lateinit var artAdapter: ArtAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GalleryActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupObservers()
        setupListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
                this@GalleryActivity,
                resources.getInteger(R.integer.gallery_span_count)
            )
            setHasFixedSize(true)
        }
    }

    private fun setupObservers() {
        viewModel.arts.observe(this) { arts ->
            artAdapter.submitList(arts)
        }

        viewModel.navigateToGeneration.observe(this) {
            startActivity(GenerationActivity.newIntent(this))
        }
    }

    private fun setupListeners() {
        binding.fabCreateArt.setOnClickListener {
            viewModel.onCreateNewArtClicked()
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.gallery_menu, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.action_refresh -> {
//                viewModel.refreshArts()
//                true
//            }
//            android.R.id.home -> {
//                onBackPressed()
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    override fun onClick(art: Art) {
        startActivity(ArtActivity.newIntent(this, art.id))
    }
}