package com.example.drawai.repo

import com.example.drawai.domain.Art
import javax.inject.Inject

class GetArtByIdUseCase @Inject constructor(
    private val repository: ArtRepository
) {
    suspend operator fun invoke(artId: Int): Art? {
        return repository.getArtById(artId)
    }
}