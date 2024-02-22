package com.olegaches.engcards.domain.use_case

import com.olegaches.engcards.domain.model.WordCard
import com.olegaches.engcards.domain.repository.WordCardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWordCardRandomListUseCase @Inject constructor(
    private val wordCardRepository: WordCardRepository
) {
    operator fun invoke(size: Int): Flow<List<WordCard>> {
        return wordCardRepository.getRandomCardList(size)
    }
}