package com.olegaches.engcards.fragment.home

import com.olegaches.engcards.domain.model.WordCard

data class HomeState(
    val cards: List<WordCard> = emptyList(),
    val loading: Boolean = false,
    val translationAttempts: Int = Int.MAX_VALUE,
    val showPremiumAd: Boolean = false,
)
