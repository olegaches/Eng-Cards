package com.olegaches.engcards.data.repository

import com.olegaches.engcards.data.local.WordCardDao
import com.olegaches.engcards.data.local.entity.WordCardEntity
import com.olegaches.engcards.data.toWordCard
import com.olegaches.engcards.data.toWordCardEntity
import com.olegaches.engcards.di.IoDispatcher
import com.olegaches.engcards.domain.model.WordCard
import com.olegaches.engcards.domain.repository.WordCardRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WordCardRepositoryImpl @Inject constructor(
    private val wordCardDao: WordCardDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : WordCardRepository {
    override fun getRandomCardList(size: Int): Flow<List<WordCard>> {
        return flow {
            var cards = wordCardDao
                .getRandomList(size)
            if (cards.isEmpty()) {
                fillDb()
                cards = wordCardDao
                    .getRandomList(size)
            }
            emit(cards.map { it.toWordCard() })
        }.flowOn(ioDispatcher)
    }

    override suspend fun addCard(wordCard: WordCard) {
        wordCardDao.insertWithTimestamp(wordCard.toWordCardEntity())
    }

    override fun getCardList(query: String?): Flow<List<WordCard>> {
        return flow {
            val cardList = if (query == null) {
                wordCardDao.getAllItems()
            } else {
                wordCardDao.searchByQuery(query)
            }.map { it.toWordCard() }

            emit(cardList)
        }.flowOn(ioDispatcher)
    }

    private suspend fun fillDb() {
        listOf(
            WordCardEntity(
                word = "chair",
                nativeTranslation = "стул"
            ),
            WordCardEntity(
                word = "table",
                nativeTranslation = "стол"
            ),
            WordCardEntity(
                word = "knife",
                nativeTranslation = "нож"
            ),
            WordCardEntity(
                word = "glass",
                nativeTranslation = "стекло"
            ),
            WordCardEntity(
                word = "sun",
                nativeTranslation = "солнце"
            ),
            WordCardEntity(
                word = "boy",
                nativeTranslation = "мальчик"
            ),
            WordCardEntity(
                word = "key",
                nativeTranslation = "ключ"
            ),
            WordCardEntity(
                word = "door",
                nativeTranslation = "дверь"
            ),
        ).forEach {
            wordCardDao.insertWithTimestamp(it)
        }
    }
}