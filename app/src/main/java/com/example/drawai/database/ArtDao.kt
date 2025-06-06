package com.example.drawai.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArtDao {
    @Query("SELECT * FROM arts ORDER BY createdAt DESC")
    suspend fun getAll(): List<ArtEntity>

    @Query("SELECT * FROM arts WHERE id = :id")
    suspend fun getById(id: Int): ArtEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(art: ArtEntity)

    @Delete
    suspend fun delete(art: ArtEntity)
}