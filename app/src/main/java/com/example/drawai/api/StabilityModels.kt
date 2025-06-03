/*
package com.example.drawai.api

import com.google.gson.annotations.SerializedName

// В том же файле или отдельном (например, StabilityModels.kt)
data class StableDiffusionRequest(
    @SerializedName("init_image")
    val initImage: String,  // Base64 без префикса "data:image/png;base64,"

    @SerializedName("text_prompts")
    val prompts: List<TextPrompt>,

    @SerializedName("image_strength")
    val imageStrength: Float = 0.35f,  // Насколько сильно изменять исходное изображение (0-1)

    @SerializedName("cfg_scale")
    val cfgScale: Int = 7,  // "Креативность" (0-20)

    @SerializedName("steps")
    val steps: Int = 30  // Качество генерации (обычно 20-50)
)

data class TextPrompt(
    @SerializedName("text")
    val text: String,

    @SerializedName("weight")
    val weight: Float = 1f
)

data class StableDiffusionResponse(
    @SerializedName("artifacts")
    val artifacts: List<Artifact>
)

data class Artifact(
    @SerializedName("base64")
    val base64Image: String  // Base64 результата
)*/
