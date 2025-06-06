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
    class GenerateArt @Inject constructor(
        private val repository: ArtRepository,
        private val yandexArtApi: ArtApi
    ) {
        suspend operator fun invoke(prompt: String): Result<Art> {
            return try {
                // 1. Отправка запроса на генерацию
                val generationRequest = ArtApi.GenerationRequest(
                    messages = listOf(ArtApi.GenerationRequest.Message(prompt)),
                    options = ArtApi.GenerationRequest.GenerationOptions()
                )

                val generationResponse = yandexArtApi.generateImage(generationRequest)
                    .takeIf { it.isSuccessful }?.body()
                    ?: return Result.failure(Exception("Generation request failed"))

                // 2. Проверка статуса операции
                var operationStatus: ArtApi.OperationStatus
                do {
                    delay(1000)
                    operationStatus = yandexArtApi.checkOperationStatus(generationResponse.operationId)
                        .takeIf { it.isSuccessful }?.body()
                        ?: return Result.failure(Exception("Operation check failed"))
                } while (!operationStatus.done)

                // 3. Обработка результата
                val imageUrl = operationStatus.response?.images?.firstOrNull()?.url
                    ?: return Result.failure(Exception("No image generated"))

                val art = Art(
                    prompt = prompt,
                    imageUrl = imageUrl,
                    createdAt = System.currentTimeMillis())
                Result.success(art)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    // Получение всех сохраненных артов
    class GetAllArts @Inject constructor(
        private val repository: ArtRepository
    ) {
        suspend operator fun invoke(): List<Art> {
            return repository.getAllArts()
        }
    }

    // Удаление арта
    class DeleteArt @Inject constructor(
        private val repository: ArtRepository
    ) {
        suspend operator fun invoke(art: Art) {
            repository.deleteArt(art)
        }
    }

    // Получение арта по ID (дополнительный полезный юзкейс)
    class GetArtById @Inject constructor(
        private val repository: ArtRepository
    ) {
        suspend operator fun invoke(id: Int): Art? {
            return repository.getArtById(id)
        }
    }
}