package com.example.drawai.domain

import com.example.drawai.api.ArtApi
import com.example.drawai.repo.ArtRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class GenerateAIArtUseCase @Inject constructor(
    private val repository: ArtRepository,
    private val yandexArtApi: ArtApi
) {
    // Генерация арта через Yandex Art API
    suspend fun generateArt(
        prompt: String,
        style: String? = null,
        resolution: String = "1024x1024"
    ): Art {
        // 1. Отправка запроса на генерацию
        val generationRequest = ArtApi.GenerationRequest(
            messages = listOf(ArtApi.GenerationRequest.Message(prompt)),
            resolution = resolution,
            options = ArtApi.GenerationRequest.GenerationOptions()
        )

        val generationResponse = yandexArtApi.generateImage(generationRequest)
            .takeIf { it.isSuccessful }?.body()
            ?: throw Exception("Generation request failed")

        // 2. Проверка статуса операции с таймаутом
        var attempt = 0
        val maxAttempts = 10
        var operationStatus: ArtApi.OperationStatus

        do {
            val delayMs = 1000L * (attempt + 1).coerceAtMost(5)
            kotlinx.coroutines.delay(delayMs)

            operationStatus = yandexArtApi.checkOperationStatus(generationResponse.operationId)
                .takeIf { it.isSuccessful }?.body()
                ?: throw Exception("Operation check failed")

            attempt++
        } while (!operationStatus.done && attempt < maxAttempts)

        if (!operationStatus.done) {
            throw Exception("Operation timeout after $maxAttempts attempts")
        }

        // 3. Получение результата
        val imageUrl = operationStatus.response?.images?.firstOrNull()?.url
            ?: throw Exception("No image generated in response")

        return Art(
            prompt = prompt,
            resolution = resolution,
            imageUrl = imageUrl,
            createdAt = System.currentTimeMillis()
        ).also {
            repository.saveArt(it) // Автосохранение при успешной генерации
        }
    }

    suspend fun getArtById(artId: Int): Art? {
        return try {
            repository.getArtById(artId)
        } catch (e: Exception) {
            null
        }
    }

    // Метод получения всех артов
    suspend fun getAllArts(): List<Art> {
        return try {
            repository.getAllArts()
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Метод удаления арта
    suspend fun deleteArt(art: Art) {
        try {
            repository.deleteArt(art)
        } catch (e: Exception) {
            // Логирование ошибки
        }
    }
}