package com.example.drawai.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ArtDao {
    @Query("SELECT * FROM artworks")
    suspend fun getAllArts(): List<ArtEntity>
    fun insertArt(artEntity: ArtEntity)
}