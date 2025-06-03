package com.example.drawai.api

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ArtApi {
    companion object {
        const val BASE_URL = "https://api.stability.ai/"
        const val API_VERSION = "v2beta"
    }

    @POST("$API_VERSION/stable-image/generate/sd3")
    @Headers("Accept: application/json")
    suspend fun generateImage(
        @Header("Authorization") auth: String = "Bearer sk-zeepJc7LBeM38XUDrStRtODcyfpU65oA72Mba1BxK4WCDfci", // Замените на реальный ключ
        @Query("prompt") prompt: String,
        @Query("image") image: String? = null, // Base64 строка
        @Query("mode") mode: String? = "text-to-image", // Или "image-to-image"
        @Query("seed") seed: Int? = 0,
        @Query("steps") steps: Int? = 30
    ): Response<StableDiffusionResponse>

    data class StableDiffusionResponse(
        @SerializedName("image") val image: String? // Base64-изображение
    )
}