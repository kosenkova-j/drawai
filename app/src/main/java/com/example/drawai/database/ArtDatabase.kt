package com.example.drawai.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ArtEntity::class], version = 1)
abstract class ArtDatabase : RoomDatabase() {
    abstract fun artDao(): ArtDao
}