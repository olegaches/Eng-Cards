package com.olegaches.engcards.domain.use_case

import com.olegaches.engcards.domain.datastore.UserPreferences
import com.olegaches.engcards.domain.model.UserPlan
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserPlanStreamUseCase @Inject constructor(
    private val userPreferences: UserPreferences
) {
    operator fun invoke(): Flow<UserPlan> {
        return userPreferences.userPlan
    }
}