package com.example.drawai

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.engine.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrawingViewModel @Inject constructor(
    private val generateAIArtUseCase: GenerateAIArtUseCase,
    private val saveArtUseCase: SaveArtUseCase
) : ViewModel() {

    private val _artGenerationState = MutableLiveData<Resource<Bitmap>>()
    val artGenerationState: LiveData<Resource<Bitmap>> = _artGenerationState

    fun generateAIArt(drawingBitmap: Bitmap) {
        _artGenerationState.postValue(Resource.Loading())
        viewModelScope.launch {
            try {
                val result = generateAIArtUseCase.execute(drawingBitmap)
                _artGenerationState.postValue(Resource.Success(result))
            } catch (e: Exception) {
                _artGenerationState.postValue(Resource.Error(e.message ?: "Error generating art"))
            }
        }
    }

    fun saveArt(original: Bitmap, generated: Bitmap) {
        viewModelScope.launch {
            saveArtUseCase.execute(original, generated)
        }
    }
}