package com.olegaches.engcards.domain.datastore

import com.olegaches.engcards.domain.model.UserPlan
import kotlinx.coroutines.flow.Flow

interface UserPreferences {
    val userPlan: Flow<UserPlan>

    suspend fun wordCardTranslationUsed()

    suspend fun wordCardAdditionUsed()

    suspend fun premiumPlanActivated()

    suspend fun freePlanActivated()

    companion object {
        const val NAME = "USER_PREFS"
    }

    suspend fun wipe()
}