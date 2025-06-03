package com.example.drawai.database

import android.graphics.Bitmap
import com.example.drawai.ArtRepository
import com.example.drawai.api.ArtApi
import com.example.drawai.api.BitmapConverter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ArtRepositoryImpl @Inject constructor(
    private val artApi: ArtApi,
    private val artDao: ArtDao,
    private val bitmapConverter: BitmapConverter
) : ArtRepository {

    override suspend fun generateAIArt(drawing: Bitmap): Bitmap {
        // Конвертируем Bitmap в Base64
        val imageBase64 = bitmapConverter.bitmapToBase64(drawing)

        // Создаем запрос для Stability AI API
        val request = ArtApi.StableDiffusionRequest(
            initImage = imageBase64,
            prompts = listOf(
                ArtApi.TextPrompt("high-quality digital art, vibrant colors, detailed", 1f),
                )
            )

        // Отправляем запрос к API
        val response = artApi.generateImage(request = request)

        if (response.isSuccessful) {
            val artifacts = response.body()?.artifacts
            if (!artifacts.isNullOrEmpty()) {
                // Берем первый результат (может быть несколько вариантов)
                val base64Result = artifacts[0].base64Image
                return bitmapConverter.base64ToBitmap(base64Result)
            } else {
                throw Exception("API returned empty response")
            }
        } else {
            throw Exception("API request failed: ${response.errorBody()?.string()}")
        }
    }

    override fun getSavedArts(): Flow<List<ArtEntity>> {
        return artDao.getAllArts()
    }

    override suspend fun saveArt(original: Bitmap, generated: Bitmap) {
        val entity = ArtEntity(
            originalImage = bitmapConverter.bitmapToByteArray(original),
            generatedImage = bitmapConverter.bitmapToByteArray(generated)
        )
        artDao.insertArt(entity)
    }
}