package com.jakana.bonialbrochures.di

import com.jakana.bonialbrochures.data.model.ContentItemAdapter
import com.jakana.bonialbrochures.data.remote.ShelfApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(ContentItemAdapter())
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl("https://mobile-s3-test-assets.aws-sdlc-bonial.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
        .build()

    @Provides @Singleton
    fun provideApi(retrofit: Retrofit): ShelfApiService
    = retrofit.create(ShelfApiService::class.java)
}