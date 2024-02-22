package com.olegaches.engcards.fragment.saved_cards

import com.olegaches.engcards.domain.model.WordCard

data class SavedCardsState(
    val loading: Boolean = false,
    val query: String = "",
    val cards: List<WordCard> = emptyList(),
    val additionAttempts: Int = Int.MAX_VALUE,
    val showPremiumAd: Boolean = false
)
