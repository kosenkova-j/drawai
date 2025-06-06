package com.example.drawai.art

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.drawai.domain.Art
import com.example.drawai.domain.GenerateAIArtUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject

@HiltViewModel
class ArtViewModel @Inject constructor(
    private val generateAIArtUseCase: GenerateAIArtUseCase,
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    private val _art = MutableLiveData<Art>()
    val art: LiveData<Art> = _art

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    fun loadArt(artId: Int) {
        viewModelScope.launch {
            generateAIArtUseCase.GetArtById()(artId)?.let { _art.value = it }
        }
    }

    fun saveArtToGallery() {
        val currentArt = _art.value ?: run {
            _toastMessage.value = "No art to save"
            return
        }

        viewModelScope.launch {
            try {
                val bitmap = withContext(Dispatchers.IO) {
                    loadBitmapFromUrl(currentArt.imageUrl)
                }

                bitmap?.let {
                    val savedUri = saveBitmapToGallery(it, "DrawAI_${System.currentTimeMillis()}.jpg")
                    _toastMessage.postValue(
                        if (savedUri != null) "Art saved to gallery"
                        else "Failed to save art"
                    )
                } ?: run {
                    _toastMessage.postValue("Couldn't load image data")
                }
            } catch (e: Exception) {
                _toastMessage.postValue("Error: ${e.localizedMessage}")
            }
        }
    }

    private suspend fun loadBitmapFromUrl(url: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                Glide.with(appContext)
                    .asBitmap()
                    .load(url)
                    .submit()
                    .get()
            } catch (e: Exception) {
                _toastMessage.postValue("Failed to load image: ${e.message}")
                null
            }
        }
    }

    private suspend fun saveBitmapToGallery(bitmap: Bitmap, filename: String): Uri? {
        return withContext(Dispatchers.IO) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // Для Android 10+ используем MediaStore
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                    }

                    val resolver = android.content.ContextWrapper().applicationContext.contentResolver
                    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                    uri?.let {
                        resolver.openOutputStream(it)?.use { os ->
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, os)
                        }
                    }
                    uri
                } else {
                    // Для старых версий Android
                    val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    val imageFile = File(imagesDir, filename)

                    FileOutputStream(imageFile).use { out ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
                    }

                    // Обновляем галерею
                    MediaStore.Images.Media.insertImage(
                        android.content.ContextWrapper().applicationContext.contentResolver,
                        imageFile.absolutePath,
                        filename,
                        "DrawAI Art"
                    )
                    Uri.fromFile(imageFile)
                }
            } catch (e: Exception) {
                null
            }
        }
    }
}