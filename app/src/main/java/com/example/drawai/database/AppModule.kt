package com.example.drawai.database

import android.content.Context
import androidx.room.Room
import com.example.drawai.ArtRepository
import com.example.drawai.api.BitmapConverter
import com.example.drawai.api.ArtApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
        ).build()
    }

    @Provides
    @Singleton
    fun provideArtDao(database: ArtDatabase): ArtDao = database.artDao()

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.example.com/") // Replace with actual API URL
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor()).build())
            .build()
    }

    @Provides
    @Singleton
    fun provideArtApi(retrofit: Retrofit): ArtApi = retrofit.create(ArtApi::class.java)

    @Provides
    @Singleton
    fun provideBitmapConverter(): BitmapConverter = BitmapConverter()
}

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @Provides
    fun provideArtRepository(
        artApi: ArtApi,
        artDao: ArtDao,
        bitmapConverter: BitmapConverter
    ): ArtRepository = ArtRepositoryImpl(artApi, artDao, bitmapConverter)
}