package com.olegaches.engcards.domain.use_case

import com.olegaches.engcards.domain.datastore.UserPreferences
import javax.inject.Inject

class WipeSettingsUseCase @Inject constructor(
    private val userPreferences: UserPreferences
) {
    suspend operator fun invoke() {
        userPreferences.wipe()
    }
}