package com.example.drawai

import android.graphics.Bitmap
import javax.inject.Inject

class GenerateAIArtUseCase @Inject constructor(
    private val artRepository: ArtRepository
) {
    suspend operator fun invoke(drawing: Bitmap): Bitmap {
        return artRepository.generateAIArt(drawing)
    }
}

class SaveArtUseCase @Inject constructor(
    private val artRepository: ArtRepository
) {
    suspend operator fun invoke(original: Bitmap, generated: Bitmap) {
        artRepository.saveArt(original, generated)
    }
}

class GetSavedArtsUseCase @Inject constructor(
    private val artRepository: ArtRepository
) {
    suspend operator fun invoke(): List<ArtEntity> {
        return artRepository.getSavedArts()
    }
}