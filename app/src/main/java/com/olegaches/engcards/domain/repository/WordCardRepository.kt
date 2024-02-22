package com.olegaches.engcards.domain.repository

import com.olegaches.engcards.domain.model.WordCard
import kotlinx.coroutines.flow.Flow

interface WordCardRepository {
    fun getRandomCardList(size: Int): Flow<List<WordCard>>

    fun getCardList(query: String?): Flow<List<WordCard>>

    suspend fun addCard(wordCard: WordCard)
}