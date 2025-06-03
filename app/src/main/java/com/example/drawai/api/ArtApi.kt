package com.example.drawai.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ArtApi {
    companion object {
        const val BASE_URL = "https://api.stability.ai/"
        const val API_KEY = "Bearer sk-zeepJc7LBeM38XUDrStRtODcyfpU65oA72Mba1BxK4WCDfci"
    }

    @POST("v1/generation/stable-diffusion-v1-6/image-to-image")
    suspend fun generateImage(
        @Header("Authorization") auth: String = API_KEY,
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Accept") accept: String = "application/json",
        @Body request: StableDiffusionRequest
    ): Response<StableDiffusionResponse>
}