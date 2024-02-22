package com.olegaches.engcards.domain.model

sealed interface UserPlan {
    data class FreePlan(
        val wordCardAdditionAttempts: Int,
        val wordCardTranslationAttempts: Int
    ) : UserPlan

    data object PremiumPlan : UserPlan
}