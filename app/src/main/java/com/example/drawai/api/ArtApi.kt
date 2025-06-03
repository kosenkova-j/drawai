package com.example.drawai.api

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.POST

interface ArtApi {
    @POST("api/v1/art-generator")
    suspend fun generateArt(
        @Body request: ArtGenerationRequest
    ): ArtGenerationResponse

    // Add other API endpoints as needed
}

data class ArtGenerationRequest(
    @SerializedName("image") val image: String,
    @SerializedName("style") val style: String
)

data class ArtGenerationResponse(
    @SerializedName("generated_image") val generatedImage: String
)