package com.example.drawai.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtDao {
    @Query("SELECT * FROM artworks ORDER BY createdAt DESC")
    fun getAllArts(): Flow<List<ArtEntity>>

    @Insert
    suspend fun insertArt(artEntity: ArtEntity)
}