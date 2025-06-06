package com.example.drawai.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artworks")
data class ArtEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val prompt: String,
    val imageUrl: String,
    val createdAt: Long = System.currentTimeMillis()
)