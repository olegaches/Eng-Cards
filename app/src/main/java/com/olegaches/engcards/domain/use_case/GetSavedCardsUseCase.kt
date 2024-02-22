package com.olegaches.engcards.domain.use_case

import com.olegaches.engcards.domain.model.WordCard
import com.olegaches.engcards.domain.repository.WordCardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedCardsUseCase @Inject constructor(
    private val repository: WordCardRepository
) {
    operator fun invoke(query: String? = null): Flow<List<WordCard>> {
        return repository.getCardList(query)
    }
}