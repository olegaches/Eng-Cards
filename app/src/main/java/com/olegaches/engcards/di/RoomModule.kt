package com.olegaches.engcards.di

import android.app.Application
import androidx.room.Room
import com.olegaches.engcards.data.local.AppDatabase
import com.olegaches.engcards.data.local.WordCardDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app, AppDatabase::class.java, AppDatabase.NAME
        )
            .build()
    }

    @Provides
    @Singleton
    fun provideCartProductsDao(
        db: AppDatabase,
    ): WordCardDao {
        return db.wordCardDao
    }
}