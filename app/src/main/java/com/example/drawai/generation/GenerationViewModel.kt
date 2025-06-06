package com.example.drawai.generation

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
class GenerationViewModel @Inject constructor(
    private val generateAIArtUseCase: GenerateAIArtUseCase,
    private val repository: ArtRepository
) : ViewModel() {

    val promptText = MutableLiveData<String>()
    val isLoading = MutableLiveData(false)
    val generatedArt = MutableLiveData<Art?>(null)
    val errorMessage = MutableLiveData<String?>(null)
    val navigateToGallery = MutableLiveData(false)

    fun generateArt() {
        val prompt = promptText.value?.trim()
        if (prompt.isNullOrEmpty()) {
            errorMessage.value = "Please enter a prompt"
            return
        }

        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null

            when (val result = GenerateAIArtUseCase.GenerateArt(
                repository,
                generateAIArtUseCase.yandexArtApi
            ).invoke(prompt)) {
                is Result.Success -> {
                    generatedArt.value = result.getOrNull()
                }
                is Result.Failure -> {
                    errorMessage.value = result.exceptionOrNull()?.message ?: "Generation failed"
                }
            }
            isLoading.value = false
        }
    }

    fun saveArt() {
        generatedArt.value?.let { art ->
            viewModelScope.launch {
                repository.saveArt(art)
                navigateToGallery.value = true
            }
        }
    }
}