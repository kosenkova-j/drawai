package com.example.drawai.repo

import android.graphics.Bitmap
import com.example.drawai.api.ArtApi
import com.example.drawai.api.BitmapConverter
import com.example.drawai.database.ArtDao
import com.example.drawai.database.ArtEntity
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import javax.inject.Inject

class ArtRepositoryImpl @Inject constructor(
    private val artApi: ArtApi,
    private val artDao: ArtDao,
    private val bitmapConverter: BitmapConverter
) : ArtRepository {

    override suspend fun generateAIArt(drawing: Bitmap): Bitmap {
        val imagePart = MultipartBody.Part.createFormData(
            "image",
            "input.png",
            bitmapConverter.bitmapToByteArray(drawing)
                .toRequestBody("image/png".toMediaTypeOrNull())
        )

        val prompt = "high-quality digital art, vibrant colors, detailed"
        val promptBody = prompt.toRequestBody("text/plain".toMediaTypeOrNull())
        val modeBody = "image-to-image".toRequestBody("text/plain".toMediaTypeOrNull())
        val stepsBody = "30".toRequestBody("text/plain".toMediaTypeOrNull())
        val strengthBody = "0.35".toRequestBody("text/plain".toMediaTypeOrNull())
        val seed = (0..4_294_967_294L).random()
        val seedBody = seed.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        val response = artApi.generateImage(
            auth = "Bearer sk-zeepJc7LBeM38XUDrStRtODcyfpU65oA72Mba1BxK4WCDfci",
            prompt = promptBody,
            image = imagePart,
            mode = modeBody,
            strength = strengthBody,
            seed = seedBody,
            steps = stepsBody
        )

        // 4. Обработка ответа
        if (!response.isSuccessful) {
            throw IOException("API error: ${response.errorBody()?.string()}")
        }

        return response.body()?.image?.let {
            bitmapConverter.base64ToBitmap(it)
        } ?: throw IOException("Empty response")
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