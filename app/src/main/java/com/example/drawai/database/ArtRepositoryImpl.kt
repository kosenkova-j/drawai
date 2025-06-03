package com.example.drawai.database

import android.graphics.Bitmap
import com.example.drawai.api.ArtApi
import com.example.drawai.api.BitmapConverter
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

    override suspend fun saveArt(original: Bitmap, generated: Bitmap) {
        val originalBytes = bitmapConverter.bitmapToByteArray(original)
        val generatedBytes = bitmapConverter.bitmapToByteArray(generated)
        artDao.insertArt(ArtEntity(originalBytes, generatedBytes))
    }

    override suspend fun getSavedArts(): List<ArtEntity> {
        return artDao.getAllArts()
    }

    override fun ArtEntity(id: ByteArray, originalImage: ByteArray): ArtEntity {
        TODO("Not yet implemented")
    }
}