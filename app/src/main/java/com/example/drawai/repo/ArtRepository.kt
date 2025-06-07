package com.example.drawai.repo

import com.example.drawai.domain.Art
import kotlinx.coroutines.flow.Flow

interface ArtRepository {
    // Основные операции
    suspend fun getAllArts(): List<Art>
    fun observeAllArts(): Flow<List<Art>>
    fun observeArtById(id: Int): Flow<Art?>
    suspend fun getArtById(id: Int): Art?
    suspend fun saveArt(art: Art): Long
    suspend fun deleteArt(art: Art)
    suspend fun clearLocalCache()

    // Генерация и получение артов
    @Throws(Exception::class)
    suspend fun generateArt(
        prompt: String,
        resolution: String = "1024x1024"
    ): Art

    @Throws(Exception::class)
    suspend fun getGeneratedArts(page: Int = 1, pageSize: Int = 20): List<Art>

    @Throws(Exception::class)
    suspend fun getArtDetails(remoteId: String): Art

    // Синхронизация
    @Throws(Exception::class)
    suspend fun refreshArts(): List<Art>

    @Throws(Exception::class)
    suspend fun syncArt(artId: Int): Art

    // Фоновые задачи
    @Throws(Exception::class)
    suspend fun downloadArtAssets(art: Art)
}