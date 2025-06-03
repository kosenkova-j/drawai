package com.example.drawai.database

import android.graphics.Bitmap

interface ArtRepository {
    suspend fun generateAIArt(drawing: Bitmap): Bitmap
    suspend fun saveArt(original: Bitmap, generated: Bitmap)
    suspend fun getSavedArts(): List<ArtEntity>
    fun ArtEntity(id: ByteArray, originalImage: ByteArray): ArtEntity
}