package com.example.drawai.art

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drawai.domain.Art
import com.example.drawai.domain.GenerateAIArtUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtViewModel @Inject constructor(
    //private val artUseCases: GenerateAIArtUseCase
) : ViewModel() {

    private val _art = MutableLiveData<Art>()
    val art: LiveData<Art> = _art

    fun loadArt(artId: Int) {
        viewModelScope.launch {
            GenerateAIArtUseCase.GetArtById()(artId)?.let { _art.value = it }
        }
    }

    fun toggleFavorite() {
        _art.value?.let { currentArt ->
            val updatedArt = currentArt.copy(isFavorite = !currentArt.isFavorite)
            _art.value = updatedArt
            viewModelScope.launch {
                GenerateAIArtUseCase.SaveArt()(updatedArt)
            }
        }
    }

    fun saveArtToGallery() {
        // Логика сохранения в галерею устройства
    }
}