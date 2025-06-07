package com.drawai.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drawai.domain.Art
import com.example.drawai.repo.ArtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val repository: ArtRepository
) : ViewModel() {
    private val _arts = MutableLiveData<List<Art>>()
    val arts: LiveData<List<Art>> = _arts

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _navigateToGeneration = MutableLiveData<Event<Boolean>>()
    val navigateToGeneration: LiveData<Event<Boolean>> = _navigateToGeneration

    init {
        loadArts()
    }

    fun loadArts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val artsList = repository.getAllArts() // Прямой вызов репозитория
                _arts.value = artsList
            } catch (e: Exception) {
                // Обработка ошибок
                _arts.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteArt(art: Art) {
        viewModelScope.launch {
            try {
                repository.deleteArt(art)
                loadArts() // Перезагружаем список после удаления
            } catch (e: Exception) {
                // Обработка ошибок удаления
            }
        }
    }

    fun refreshArts() {
        loadArts()
    }

    fun onCreateNewArtClicked() {
        _navigateToGeneration.value = Event(true)
    }
}

// Класс-обертка для событий навигации (предотвращает многократные срабатывания)
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