package com.example.drawai.generation

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.drawai.api.Resource
import com.example.drawai.database.ArtEntity
import com.example.drawai.domain.GenerateAIArtUseCase
import com.example.drawai.domain.GetSavedArtsUseCase
import com.example.drawai.domain.SaveArtUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrawingViewModel @Inject constructor(
    private val generateAIArtUseCase: GenerateAIArtUseCase,
    private val saveArtUseCase: SaveArtUseCase,
    private val getSavedArtsUseCase: GetSavedArtsUseCase
) : ViewModel() {

    // Для генерации изображений
    private val _artGenerationState = MutableLiveData<Resource<Bitmap>>()
    val artGenerationState: LiveData<Resource<Bitmap>> = _artGenerationState

    // Для списка сохраненных артов (используем Flow напрямую)
    val savedArts: Flow<List<ArtEntity>> = getSavedArtsUseCase()

    // Если нужен LiveData для наблюдения во View
    val savedArtsLiveData: LiveData<List<ArtEntity>> = savedArts.asLiveData()

    fun generateAIArt(drawingBitmap: Bitmap) {
        _artGenerationState.postValue(Resource.Loading())
        viewModelScope.launch {
            try {
                val result = generateAIArtUseCase(drawingBitmap)
                _artGenerationState.postValue(Resource.Success(result))
            } catch (e: Exception) {
                _artGenerationState.postValue(Resource.Error(e.message ?: "Error generating art"))
            }
        }
    }

    fun saveArt(original: Bitmap, generated: Bitmap) {
        viewModelScope.launch {
            saveArtUseCase(original, generated)
            // Flow автоматически обновится при изменении в БД
        }
    }
}