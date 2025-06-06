package com.example.drawai.art

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.drawai.R
import com.example.drawai.art.ArtViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class ArtActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArtDetailBinding // Используйте правильное имя класса
    private val viewModel: ArtViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtDetailBinding.inflate(layoutInflater) // Убедитесь, что имя класса правильное
        setContentView(binding.root)
        binding.activity = this

        setupToolbar()
        observeViewModel()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun observeViewModel() {
        val artId = intent.getIntExtra(EXTRA_ART_ID, -1)
        viewModel.loadArt(artId)

        viewModel.art.observe(this) { art ->
            art?.let {
                binding.art = it
                updateFavoriteButton(it.isFavorite)
            }
        }
    }

    private fun updateFavoriteButton(isFavorite: Boolean) {
        binding.favButton.setImageResource(
            if (isFavorite) R.drawable.ic_favorite_filled
            else R.drawable.ic_favorite_outline
        )
        binding.favButton.imageTintList = ColorStateList.valueOf(
            ContextCompat.getColor(this,
                if (isFavorite) R.color.red_500
                else R.color.white
            )
        )
    }

    private fun setupClickListeners() {
        binding.saveButton.setOnClickListener {
            viewModel.saveArtToGallery()
            Toast.makeText(this, "Art saved", Toast.LENGTH_SHORT).show()
        }

        binding.shareButton.setOnClickListener {
            shareArtImage(binding.artImage.drawable)
        }

        binding.favButton.setOnClickListener {
            viewModel.toggleFavorite()
        }
    }

    private fun shareArtImage(drawable: Drawable?) {
        drawable?.let {
            val bitmap = (it as BitmapDrawable).bitmap
            val uri = bitmap.getShareableUri(this)
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                type = "image/*"
            }
            startActivity(Intent.createChooser(shareIntent, "Share Art"))
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

// Extension function для Bitmap
fun Bitmap.getShareableUri(context: Context): Uri {
    val file = File(context.cacheDir, "share_art_${System.currentTimeMillis()}.png")
    FileOutputStream(file).use { out ->
        this.compress(Bitmap.CompressFormat.PNG, 100, out)
    }
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
}