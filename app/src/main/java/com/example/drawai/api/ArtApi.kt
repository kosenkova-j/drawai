package com.example.drawai.api

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ArtApi {
    companion object {
        const val BASE_URL = "https://api.stability.ai/"
        const val API_VERSION = "v2beta"
    }

    @Multipart
    @POST("$API_VERSION/stable-image/generate/sd3")
    suspend fun generateImage(
        @Header("Authorization") auth: String,
        @Part("prompt") prompt: RequestBody,
        @Part image: MultipartBody.Part?,
        @Part("mode") mode: RequestBody,
        @Part("seed") seed: RequestBody?,
        @Part("steps") steps: RequestBody?
    ): Response<StableDiffusionResponse>

    // Модель ответа
    data class StableDiffusionResponse(
        @field:SerializedName("image") val image: String?
    )

    // Модель ошибки
    data class ApiError(
        @field:SerializedName("message") val message: String?,
        @field:SerializedName("errors") val errors: List<String>?
    )
}