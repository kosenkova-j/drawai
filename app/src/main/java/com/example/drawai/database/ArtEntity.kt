package com.example.drawai.database

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase

@Entity(tableName = "artworks")
data class ArtEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "original_image") val originalImage: ByteArray,
    @ColumnInfo(name = "generated_image") val generatedImage: ByteArray,
    @ColumnInfo(name = "created_at") val createdAt: Long
)

@Dao
interface ArtDao {
    @Insert
    suspend fun insertArt(art: ArtEntity)

    @Query("SELECT * FROM artworks ORDER BY created_at DESC")
    suspend fun getAllArts(): List<ArtEntity>
}

@Database(entities = [ArtEntity::class], version = 1)
abstract class ArtDatabase : RoomDatabase() {
    abstract fun artDao(): ArtDao
}