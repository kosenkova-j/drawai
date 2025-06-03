package com.example.drawai.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artworks")
data class ArtEntity(
    @PrimaryKey(autoGenerate = true) val id: ByteArray = 0,
    val originalImage: ByteArray,  // Байтовый массив оригинального рисунка
    val generatedImage: ByteArray, // Байтовый массив сгенерированного изображения
    val createdAt: Long = System.currentTimeMillis()
) {
    // Для правильного сравнения объектов
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ArtEntity
        if (id != other.id) return false
        if (!originalImage.contentEquals(other.originalImage)) return false
        if (!generatedImage.contentEquals(other.generatedImage)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + originalImage.contentHashCode()
        result = 31 * result + generatedImage.contentHashCode()
        return result
    }
}