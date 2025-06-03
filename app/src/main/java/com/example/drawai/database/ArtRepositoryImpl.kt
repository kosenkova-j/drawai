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
        val imageBase64 = bitmapConverter.bitmapToBase64(drawing)
        val response = artApi.generateArt(imageBase64)
        return bitmapConverter.base64ToBitmap(response.generatedImage)
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