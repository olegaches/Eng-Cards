package com.olegaches.engcards.di

import com.olegaches.engcards.data.local.datastore.UserPreferencesImpl
import com.olegaches.engcards.domain.datastore.UserPreferences
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class UserPreferencesModule {
    @Binds
    @Singleton
    abstract fun provideUserPrefs(
        userPreferencesImpl: UserPreferencesImpl
    ): UserPreferences
}