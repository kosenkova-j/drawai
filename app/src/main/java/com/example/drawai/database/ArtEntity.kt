package com.example.drawai.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "arts")
data class ArtEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val prompt: String,
    val resolution : String,
    val imageUrl: String,
    val createdAt: Long = System.currentTimeMillis()
)
