package com.example.drawai.database

import com.google.gson.annotations.SerializedName

data class ArtResponse(
    @SerializedName("images") val images: List<ArtImage>
) {
    data class ArtImage(
        @SerializedName("url") val url: String,
        @SerializedName("seed") val seed: Long
    )
}