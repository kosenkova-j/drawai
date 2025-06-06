package com.example.drawai.repo

import android.graphics.Bitmap
import com.example.drawai.api.ArtApi
import com.example.drawai.database.ArtDao
import com.example.drawai.database.ArtEntity
import com.example.drawai.domain.Art
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import javax.inject.Inject

class ArtworkRepositoryImpl @Inject constructor(
    private val remoteDataSource: ArtworkRemoteDataSource,
    private val localDataSource: ArtDao
) : ArtRepository {
    override suspend fun generateArtwork(prompt: String, token: String): Art {
        return remoteDataSource.generateArtwork(prompt, token)
    }

    override suspend fun getArtworks(): List<Art> {
        return localDataSource.getAll().map { it.toDomain() }
    }

    override suspend fun saveArtwork(artwork: Art) {
        localDataSource.insert(artwork.toEntity())
    }

    override suspend fun deleteArtwork(artwork: Art) {
        localDataSource.delete(artwork.toEntity())
    }
}