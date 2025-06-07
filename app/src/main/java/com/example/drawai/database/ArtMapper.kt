package com.example.drawai.database

import com.example.drawai.domain.Art
import javax.inject.Inject

class ArtMapper @Inject constructor() {
    fun toDomain(entity: ArtEntity): Art = Art(
        id = entity.id,
        prompt = entity.prompt,
        imageUrl = entity.imageUrl,
        resolution = entity.resolution,
        createdAt = entity.createdAt
    )

    fun toEntity(domain: Art): ArtEntity = ArtEntity(
        id = domain.id,
        prompt = domain.prompt,
        imageUrl = domain.imageUrl,
        resolution = domain.resolution ?: "1024x1024",
        createdAt = domain.createdAt
    )
}