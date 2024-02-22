package com.olegaches.engcards.domain.use_case

import com.olegaches.engcards.di.IoDispatcher
import com.olegaches.engcards.domain.datastore.UserPreferences
import com.olegaches.engcards.domain.model.WordCard
import com.olegaches.engcards.domain.repository.WordCardRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreateWordCardUseCase @Inject constructor(
    private val repository: WordCardRepository,
    private val userPreferences: UserPreferences,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(wordCard: WordCard) {
        withContext(ioDispatcher) {
            repository.addCard(wordCard)
            userPreferences.wordCardAdditionUsed()
        }
    }
}