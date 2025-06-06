package com.example.drawai.database

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ArtDatabase {
        return ArtDatabase.getInstance(context)
    }

    @Provides
    fun provideArtDao(database: ArtDatabase): ArtDao {
        return database.artDao()
    }
}