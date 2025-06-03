package com.example.drawai

import com.example.drawai.api.ArtApi
import com.example.drawai.api.BitmapConverter
import com.example.drawai.database.ArtDao
import com.example.drawai.database.ArtRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class) // Ограничиваем время жизни зависимостей ViewModel
object ViewModelModule {

//    @Provides
//    fun provideArtRepository(
//        artApi: ArtApi,
//        artDao: ArtDao,
//        bitmapConverter: BitmapConverter
//    ): ArtRepository = ArtRepositoryImpl(artApi, artDao, bitmapConverter)

    @Provides
    fun provideGenerateAIArtUseCase(repository: ArtRepository): GenerateAIArtUseCase {
        return GenerateAIArtUseCase(repository)
    }

    @Provides
    fun provideSaveArtUseCase(repository: ArtRepository): SaveArtUseCase {
        return SaveArtUseCase(repository)
    }

    @Provides
    fun provideGetSavedArtsUseCase(repository: ArtRepository): GetSavedArtsUseCase {
        return GetSavedArtsUseCase(repository)
    }
}