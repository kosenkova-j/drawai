package com.example.drawai.domain

import com.example.drawai.domain.Art
import com.example.drawai.repo.ArtRepository
import javax.inject.Inject

class GenerateAIArtUseCase @Inject constructor(
    private val repository: ArtRepository
) {
    // UseCase для генерации artwork
    class GenerateArt @Inject constructor(
        private val repository: ArtRepository
    ) {
        suspend operator fun invoke(prompt: String, token: String): Result<Art> {
            return try {
                val art = repository.generateArt(prompt, token)
                Result.success(art)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    // UseCase для получения всех artworks
    class GetArts @Inject constructor(
        private val repository: ArtRepository
    ) {
        suspend operator fun invoke(): List<Art> {
            return repository.getArts()
        }
    }

    // UseCase для сохранения artwork
    class SaveArt @Inject constructor(
        private val repository: ArtRepository
    ) {
        suspend operator fun invoke(art: Art) {
            repository.saveArt(art)
        }
    }

    // UseCase для удаления artwork
    class DeleteArt @Inject constructor(
        private val repository: ArtRepository
    ) {
        suspend operator fun invoke(art: Art) {
            repository.deleteArt(art)
        }
    }

    // UseCase для получения конкретного artwork по ID
    class GetArtById @Inject constructor(
        private val repository: ArtRepository
    ) {
        suspend operator fun invoke(id: Int): Art? {
            return repository.getArts().firstOrNull { it.id == id }
        }
    }
}