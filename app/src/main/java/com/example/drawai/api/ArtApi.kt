package com.example.drawai.api

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ArtApi {
    @POST("generate")
    suspend fun generateArtwork(
        @Header("Authorization") token: String,
        @Body request: GenerationRequest
    ): Response<ArtworkResponse>

    data class GenerationRequest(val prompt: String)
}

data class ArtworkResponse(
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("prompt") val prompt: String
)