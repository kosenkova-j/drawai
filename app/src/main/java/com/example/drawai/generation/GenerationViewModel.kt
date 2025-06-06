package com.example.drawai.generation

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
class GenerationViewModel @Inject constructor(
    private val artUseCases: GenerateAIArtUseCase
) : ViewModel() {
    private val _generatedArt = MutableLiveData<Art?>()
    val generatedArt: LiveData<Art?> = _generatedArt

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun generateArt(prompt: String, token: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = artUseCases.GenerateArt()(prompt, token)
                _generatedArt.value = result.getOrNull()
                _error.value = result.exceptionOrNull()?.message
            } catch (e: Exception) {
                _error.value = e.message
                _generatedArt.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}