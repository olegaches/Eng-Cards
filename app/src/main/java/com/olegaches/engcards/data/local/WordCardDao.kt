package com.olegaches.engcards.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.olegaches.engcards.data.local.entity.WordCardEntity

@Dao
abstract class WordCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(wordCard: WordCardEntity)

    suspend fun insertWithTimestamp(wordCard: WordCardEntity) {
        insert(wordCard.copy(timestamp = System.currentTimeMillis()))
    }

    @Transaction
    @Query("SELECT * FROM $TABLE_NAME ORDER BY RANDOM() LIMIT :size")
    abstract suspend fun getRandomList(size: Int): List<WordCardEntity>

    companion object {
        const val TABLE_NAME = "word_card"
    }
}