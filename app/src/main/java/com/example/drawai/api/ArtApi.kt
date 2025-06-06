package com.example.drawai.api

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ArtApi {
    @POST("foundationModels/v1/imageGenerationAsync")
    suspend fun generateImage(
        @Body request: GenerationRequest
    ): Response<GenerationResponse>

    @GET("operations/{operationId}")
    suspend fun checkOperationStatus(
        @Path("operationId") operationId: String
    ): Response<OperationStatus>

    data class GenerationRequest(
        @SerializedName("model") val model: String = "art://b1gfib2713qvl7j5rrd7/yandex-art/latest",
        @SerializedName("messages") val messages: List<Message>,
        @SerializedName("generationOptions") val options: GenerationOptions
    ) {
        data class Message(
            @SerializedName("text") val text: String,
            @SerializedName("weight") val weight: Double = 1.0
        )

        data class GenerationOptions(
            @SerializedName("seed") val seed: Long? = null,
            @SerializedName("temperature") val temperature: Double = 0.5
        )
    }

    data class GenerationResponse(
        @SerializedName("id") val operationId: String
    )

    data class OperationStatus(
        @SerializedName("done") val done: Boolean,
        @SerializedName("response") val response: ArtResponse?,
        @SerializedName("error") val error: String?
    ) {
        data class ArtResponse(
            @SerializedName("images") val images: List<ArtImage>
        ) {
            data class ArtImage(
                @SerializedName("url") val url: String,
                @SerializedName("seed") val seed: Long
            )
        }
    }
}