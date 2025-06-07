package com.example.drawai.repo

import com.example.drawai.api.ArtApi
import com.example.drawai.database.ArtDao
import com.example.drawai.database.ArtMapper
import com.example.drawai.domain.Art
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArtRepositoryImpl @Inject constructor(
    private val localDataSource: ArtDao,
    private val remoteDataSource: ArtApi,
    private val mapper: ArtMapper
) : ArtRepository {

    override suspend fun clearLocalCache() {
        localDataSource.clearAll() // Предполагая, что ArtDao имеет такой метод
    }

    // Flow операции остаются без изменений
    override fun observeAllArts(): Flow<List<Art>> {
        return localDataSource.observeAll().map { arts ->
            arts.map { mapper.toDomain(it) }
        }
    }

    override fun observeArtById(id: Int): Flow<Art?> {
        return localDataSource.observeById(id).map { it?.let(mapper::toDomain) }
    }

    // Базовые операции с БД
    override suspend fun getArtById(id: Int): Art? {
        return localDataSource.getById(id)?.let(mapper::toDomain)
    }

    override suspend fun saveArt(art: Art): Long {
        return localDataSource.insert(mapper.toEntity(art))
    }

    override suspend fun deleteArt(art: Art) {
        localDataSource.delete(mapper.toEntity(art))
    }

    // Генерация арта
    override suspend fun generateArt(
        prompt: String,
        resolution: String
    ): Art {
        // 1. Инициируем генерацию
        val response = remoteDataSource.generateImage(
            ArtApi.GenerationRequest(
                messages = listOf(ArtApi.GenerationRequest.Message(prompt)),
                resolution = resolution,
                options = ArtApi.GenerationRequest.GenerationOptions()
            )
        ).takeIf { it.isSuccessful }?.body()
            ?: throw Exception("Generation request failed")

        // 2. Проверяем статус операции
        var attempt = 0
        val maxAttempts = 10
        var currentDelay = 1.seconds

        while (attempt < maxAttempts) {
            delay(currentDelay)
            val status = remoteDataSource.checkOperationStatus(response.operationId)
                .takeIf { it.isSuccessful }?.body()
                ?: throw Exception("Operation check failed")

            if (status.done) {
                // 3. Создаем и сохраняем арт
                val imageUrl = status.response?.images?.firstOrNull()?.url
                    ?: throw Exception("No image generated")

                return Art(
                    prompt = prompt,
                    resolution = resolution,
                    imageUrl = imageUrl,
                    createdAt = System.currentTimeMillis()
                ).also { art ->
                    saveArt(art)
                }
            }

            currentDelay = (currentDelay * 2).coerceAtMost(8.seconds)
            attempt++
        }

        throw Exception("Operation timeout after $maxAttempts attempts")
    }

    // Дополнительные операции
    override suspend fun getGeneratedArts(page: Int, pageSize: Int): List<Art> {
        return localDataSource.getAll().map { mapper.toDomain(it) }
    }

    override suspend fun getArtDetails(remoteId: String): Art {
        throw UnsupportedOperationException("Not implemented")
    }

    override suspend fun downloadArtAssets(art: Art) {
        // Реализация загрузки ассетов
    }

    // Синхронизация
    override suspend fun refreshArts(): List<Art> {
        return localDataSource.getAll().map { mapper.toDomain(it) }
    }

    override suspend fun syncArt(artId: Int): Art {
        return getArtById(artId) ?: throw Exception("Art not found")
    }

    override suspend fun getAllArts(): List<Art> {
        return localDataSource.getAll().map { mapper.toDomain(it) }
    }
}