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
    val navigateToGallery = MutableLiveData<Event<Boolean>>()  // Используем Event для навигации

    fun generateArt() {
        val prompt = promptText.value?.trim()
        if (prompt.isNullOrEmpty()) {
            errorMessage.value = "Please enter a prompt"
            return
        }

        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            generatedArt.value = null

            try {
                val art = generateAIArtUseCase.generateArt(prompt)
                generatedArt.value = art
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "Art generation failed"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun saveArt() {
        generatedArt.value?.let { art ->
            viewModelScope.launch {
                try {
                    repository.saveArt(art)
                    navigateToGallery.value = Event(true)  // Используем Event
                } catch (e: Exception) {
                    errorMessage.value = "Failed to save art: ${e.message}"
                }
            }
        } ?: run {
            errorMessage.value = "No art to save"
        }
    }
}

// Класс-обертка для событий (предотвращает многократные срабатывания)
class Event<T>(private val content: T) {
    private var hasBeenHandled = false

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
}