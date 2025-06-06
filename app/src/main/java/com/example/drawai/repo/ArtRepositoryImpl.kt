package com.example.drawai.data.repository

import com.example.drawai.api.ArtApi
import com.example.drawai.database.ArtDao
import com.example.drawai.database.ArtMapper
import com.example.drawai.domain.Art
import com.example.drawai.domain.ResultState
import com.example.drawai.repo.ArtRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ArtRepositoryImpl @Inject constructor(
    private val localDataSource: ArtDao,
    private val remoteDataSource: ArtApi,
    private val mapper: ArtMapper
) : ArtRepository {

    // ========== Локальные операции ==========
    override suspend fun getAllArts(): List<Art> {
        return localDataSource.getAll().map { mapper.toDomain(it) }
    }

    override suspend fun getArtById(id: Int): Art? {
        return localDataSource.getById(id)?.let { mapper.toDomain(it) }
    }

    override suspend fun saveArt(art: Art) {
        localDataSource.insert(mapper.toEntity(art))
    }

    override suspend fun deleteArt(art: Art) {
        localDataSource.delete(mapper.toEntity(art))
    }

    // ========== Удаленные операции ==========
    override suspend fun generateArt(prompt: String): ResultState<Art> {
        return try {
            // 1. Генерация через API
            val response = remoteDataSource.generateImage(
                ArtApi.GenerationRequest(
                    messages = listOf(ArtApi.GenerationRequest.Message(prompt)),
                    options = ArtApi.GenerationRequest.GenerationOptions()
                )
            ).takeIf { it.isSuccessful }?.body()
                ?: return ResultState.Error("Generation request failed")

            // 2. Проверка статуса операции
            var operation: ArtApi.OperationStatus
            do {
                operation = remoteDataSource.checkOperationStatus(response.operationId)
                    .takeIf { it.isSuccessful }?.body()
                    ?: return ResultState.Error("Operation check failed")

                kotlinx.coroutines.delay(1000)
            } while (!operation.done)

            // 3. Обработка результата
            val imageUrl = operation.response?.images?.firstOrNull()?.url
                ?: return ResultState.Error("No image generated")

            val art = Art(prompt = prompt, imageUrl = imageUrl, createdAt = System.currentTimeMillis())
            ResultState.Success(art)
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Unknown error")
        }
    }

    // ========== Комбинированные операции ==========
    override suspend fun refreshArts(): ResultState<List<Art>> {
        return try {
            // В реальном приложении здесь может быть синхронизация с бэкендом
            val localArts = localDataSource.getAll().map { mapper.toDomain(it) }
            ResultState.Success(localArts)
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Failed to refresh arts")
        }
    }
}