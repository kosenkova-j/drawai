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
        val imagePart = bitmapConverter.bitmapToMultipart(drawing, "image")

        val response = artApi.generateImage(
            auth = "Bearer sk-zeepJc7LBeM38XUDrStRtODcyfpU65oA72Mba1BxK4WCDfci", // Лучше через DI
            prompt = "high-quality digital art",
            image = imagePart,
            mode = "image-to-image",
            steps = 30
        )

        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()
            throw Exception("API error: $errorBody")
        }

        return response.body()?.image?.let {
            bitmapConverter.base64ToBitmap(it)
        } ?: throw Exception("Empty response")
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