package com.jakana.bonialbrochures.di

import com.jakana.bonialbrochures.data.repository.BrochureRepositoryImpl
import com.jakana.bonialbrochures.domain.repository.BrochureRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindRepository(impl: BrochureRepositoryImpl): BrochureRepository
}