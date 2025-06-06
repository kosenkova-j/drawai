package com.drawai.gallery

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
    private val artUseCases: ArtUseCases
) : ViewModel() {
    private val _arts = MutableStateFlow<List<Art>>(emptyList())
    val arts: StateFlow<List<Art>> = _arts

    init {
        loadArts()
    }

    fun loadArts() {
        viewModelScope.launch {
            _arts.value = artUseCases.GetAllArts()()
        }
    }

    fun deleteArt(art: Art) {
        viewModelScope.launch {
            artUseCases.DeleteArt()(art)
            loadArts()
        }
    }
}