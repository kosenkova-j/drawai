package com.example.drawai

import android.content.Context
import androidx.room.Room
import com.example.drawai.api.ArtApi
import com.example.drawai.database.ArtDao
import com.example.drawai.database.ArtDatabase
import com.example.drawai.repo.ArtRepository
import com.example.drawai.repo.GetArtByIdUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://llm.api.cloud.yandex.net/"
    private const val FOLDER_ID = "b1gfib2713qvl7j5rrd7" // Замените на ваш folder_id
    private const val MODEL_ID = "art" // Идентификатор модели Yandex ART

    @Provides
    fun provideGetArtByIdUseCase(repository: ArtRepository): GetArtByIdUseCase {
        return GetArtByIdUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("Authorization", "Bearer t1.9euelZqQzIqWl8qWmZqTzJfLiprOne3rnpWax5SPk8yQz4qSypWbk8uckcrl8_d9a3M9-e9sHi4L_d3z9z0acT3572weLgv9zef1656Vmsuez8aZz5nHmM2SkJXKisfI7_zF656Vmsuez8aZz5nHmM2SkJXKisfI.yp2lyDz_gCC6LMJ3tn9nwLaFJhx84PxTIp7eBJFm212Vodcvy3B9QrFVaPc3I8Sf8LOAEBlYXQ-ufzvHHlh8Cw")
                .header("x-folder-id", FOLDER_ID)
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideArtApi(retrofit: Retrofit): ArtApi {
        return retrofit.create(ArtApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ArtDatabase {
        return Room.databaseBuilder(
            context,
            ArtDatabase::class.java,
            "drawai-db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideArtDao(database: ArtDatabase): ArtDao = database.artDao()
}