package com.drawai.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drawai.domain.Art
import com.example.drawai.domain.GenerateAIArtUseCase
import com.example.drawai.repo.ArtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    //private val generateAIArtUseCase: GenerateAIArtUseCase
    private val repository: ArtRepository
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
            _arts.value = GenerateAIArtUseCase.GetAllArts(repository)()
            _isLoading.value = false
        }
    }

    fun deleteArt(art: Art) {
        viewModelScope.launch {
            GenerateAIArtUseCase.DeleteArt(repository)(art)
            loadArts()
        }
    }

    fun refreshArts() {
        loadArts()
    }


    fun onCreateNewArtClicked() {
        _navigateToGeneration.value = true
    }
}