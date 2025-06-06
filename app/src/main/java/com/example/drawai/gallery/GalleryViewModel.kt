package com.drawai.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drawai.domain.Art
import com.example.drawai.domain.GenerateAIArtUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val artUseCases: GenerateAIArtUseCase
) : ViewModel() {
    private val _arts = MutableLiveData<List<Art>>()
    val arts: LiveData<List<Art>> = _arts

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _navigateToGeneration = MutableLiveData(false)
    val navigateToGeneration: LiveData<Boolean> = _navigateToGeneration

    init {
        loadArts()
    }

    fun loadArts() {
        viewModelScope.launch {
            _isLoading.value = true
            _arts.value = artUseCases.GetAllArts()()
            _isLoading.value = false
        }
    }

    fun refreshArts() {
        loadArts()
    }

    fun deleteArt(art: Art) {
        viewModelScope.launch {
            artUseCases.DeleteArt()(art)
            loadArts() // Обновляем список после удаления
        }
    }

    fun onCreateNewArtClicked() {
        _navigateToGeneration.value = true
    }
}