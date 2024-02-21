package com.olegaches.engcards.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.olegaches.engcards.data.local.entity.WordCardEntity

@Database(
    entities = [WordCardEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract val wordCardDao: WordCardDao

    companion object {
        const val NAME = "app_dp"
    }
}