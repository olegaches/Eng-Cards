package com.olegaches.engcards.di

import com.olegaches.engcards.data.repository.WordCardRepositoryImpl
import com.olegaches.engcards.domain.repository.WordCardRepository
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
    abstract fun bindWordCardRepository(
        wordCardRepositoryImpl: WordCardRepositoryImpl
    ): WordCardRepository
}