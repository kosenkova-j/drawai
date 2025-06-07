package com.example.drawai.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtDao {
    @Query("SELECT * FROM arts")
    fun observeAll(): Flow<List<ArtEntity>>

    @Query("SELECT * FROM arts WHERE id = :id")
    fun observeById(id: Int): Flow<ArtEntity?>

    @Query("SELECT * FROM arts ORDER BY createdAt DESC")
    suspend fun getAll(): List<ArtEntity>

    @Query("SELECT * FROM arts WHERE id = :id")
    suspend fun getById(id: Int): ArtEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(art: ArtEntity): Long  

    @Delete
    suspend fun delete(art: ArtEntity)

    @Query("DELETE FROM arts")
    suspend fun clearAll()
}