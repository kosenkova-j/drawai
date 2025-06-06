package com.example.drawai.repo

import android.graphics.Bitmap
import com.example.drawai.database.ArtEntity
import kotlinx.coroutines.flow.Flow

interface ArtRepository {
    suspend fun generateAIArt(drawing: Bitmap): Bitmap
    fun getSavedArts(): Flow<List<ArtEntity>>
    suspend fun saveArt(original: Bitmap, generated: Bitmap)
}