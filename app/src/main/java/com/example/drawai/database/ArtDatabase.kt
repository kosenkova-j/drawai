package com.example.drawai.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [ArtEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ArtDatabase : RoomDatabase() {
    abstract fun artDao(): ArtDao

    companion object {
        @Volatile
        private var instance: ArtDatabase? = null

        fun getInstance(context: Context): ArtDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): ArtDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ArtDatabase::class.java,
                "arts.db"
            )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                    }
                })
                .build()
        }
    }
}