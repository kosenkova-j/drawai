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
        // Конвертируем Bitmap в base64
        val imageBase64 = bitmapConverter.bitmapToBase64(drawing)

        // Делаем запрос к API
        val response = artApi.generateImage(
            prompt = "high-quality digital art, vibrant colors, detailed",
            image = imageBase64,
            mode = "image-to-image",
            steps = 30
        )

        if (response.isSuccessful) {
            response.body()?.image?.let { base64Image ->
                return bitmapConverter.base64ToBitmap(base64Image)
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