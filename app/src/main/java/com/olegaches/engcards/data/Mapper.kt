package com.olegaches.engcards.data

import com.olegaches.engcards.data.local.entity.WordCardEntity
import com.olegaches.engcards.domain.model.WordCard

fun WordCardEntity.toWordCard(): WordCard {
    return WordCard(
        word = word,
        nativeTranslation = nativeTranslation
    )
}