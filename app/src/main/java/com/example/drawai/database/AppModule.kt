package com.example.drawai.database

import android.content.Context
import androidx.room.Room
import com.example.drawai.api.BitmapConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideArtDatabase(@ApplicationContext context: Context): ArtDatabase {
        return Room.databaseBuilder(
            context,
            ArtDatabase::class.java,
            "art_db"
        ).fallbackToDestructiveMigration() // Добавлено для миграций
            .build()
    }

    @Provides
    @Singleton
    fun provideArtDao(database: ArtDatabase): ArtDao = database.artDao()

    @Provides
    @Singleton
    fun provideBitmapConverter(): BitmapConverter = BitmapConverter()
}