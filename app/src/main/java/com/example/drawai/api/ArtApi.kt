package com.example.drawai.api

import com.google.gson.annotations.SerializedName
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
        @Header("Authorization") auth: String,
        @Body request: StableDiffusionRequest
    ): Response<StableDiffusionResponse>

    data class StableDiffusionRequest(
        @SerializedName("init_image") val initImage: String,
        @SerializedName("text_prompts") val prompts: List<TextPrompt>,
        @SerializedName("image_strength") val imageStrength: Float,
        @SerializedName("steps") val steps: Int
    )

    data class TextPrompt(
        @SerializedName("text") val text: String,
        @SerializedName("weight") val weight: Float = 1f
    )

    data class StableDiffusionResponse(
        @SerializedName("artifacts") val artifacts: List<Artifact>
    )

    data class Artifact(
        @SerializedName("base64") val base64Image: String
    )
}
