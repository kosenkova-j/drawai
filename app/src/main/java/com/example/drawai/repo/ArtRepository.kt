package com.example.drawai.repo

import com.example.drawai.domain.Art
import com.example.drawai.domain.ResultState

interface ArtRepository {
    // Локальные операции (Room)
    suspend fun getAllArts(): List<Art>
    suspend fun getArtById(id: Int): Art?
    suspend fun saveArt(art: Art)
    suspend fun deleteArt(art: Art)

    // Удаленные операции (Yandex Art API)
    suspend fun generateArt(prompt: String): ResultState<Art>

    // Комбинированные операции
    suspend fun refreshArts(): ResultState<List<Art>>
}