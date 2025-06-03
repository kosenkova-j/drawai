package com.example.drawai.database

import android.graphics.Bitmap
import com.example.drawai.ArtRepository
import com.example.drawai.api.ArtApi
import com.example.drawai.api.ArtApi.Companion.API_KEY
import com.example.drawai.api.BitmapConverter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ArtRepositoryImpl @Inject constructor(
    private val artApi: ArtApi,
    private val artDao: ArtDao,
    private val bitmapConverter: BitmapConverter
) : ArtRepository {

    override suspend fun generateAIArt(drawing: Bitmap): Bitmap {
        val imageBase64 = bitmapConverter.bitmapToBase64(drawing)

        val request = ArtApi.StableDiffusionRequest(
            initImage = imageBase64,
            prompts = listOf(
                ArtApi.TextPrompt("high-quality digital art, vibrant colors, detailed", 1f)
            ),
            imageStrength = 0.35f,
            steps = 30
        )

        val response = artApi.generateImage(
            auth = API_KEY,
            request = request
        )

        if (response.isSuccessful) {
            response.body()?.artifacts?.firstOrNull()?.base64Image?.let {
                return bitmapConverter.base64ToBitmap(it)
            } ?: throw Exception("Empty API response")
        } else {
            throw Exception("API error: ${response.errorBody()?.string()}")
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