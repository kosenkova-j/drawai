package com.example.drawai.domain

sealed class ResultState<out T> {
    object Loading : ResultState<Nothing>() // Состояние загрузки
    data class Success<out T>(val data: T) : ResultState<T>() // Успешный результат
    data class Error(val message: String) : ResultState<Nothing>() // Ошибка
}