package com.olegaches.engcards.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.olegaches.engcards.data.local.WordCardDao.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class WordCardEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = 0,
    val word: String,
    val nativeTranslation: String
)