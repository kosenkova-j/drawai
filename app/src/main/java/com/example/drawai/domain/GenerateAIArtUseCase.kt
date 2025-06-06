package com.example.drawai.domain

import android.graphics.Bitmap
import com.example.drawai.database.ArtEntity
import com.example.drawai.repo.ArtRepository
import kotlinx.coroutines.flow.Flow
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
    operator fun invoke(): Flow<List<ArtEntity>> {
        return artRepository.getSavedArts()
    }
}