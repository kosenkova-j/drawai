package com.example.drawai.repo

import com.example.drawai.data.repository.ArtRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindArtRepository(
        impl: ArtRepositoryImpl
    ): ArtRepository
}