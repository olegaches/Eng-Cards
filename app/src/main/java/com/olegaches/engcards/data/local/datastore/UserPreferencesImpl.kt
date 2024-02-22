package com.olegaches.engcards.data.local.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import com.olegaches.engcards.domain.datastore.UserPreferences
import com.olegaches.engcards.domain.model.UserPlan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesImpl @Inject constructor(
    private val dataStorePrefs: DataStore<Preferences>
) : UserPreferences {
    private val tag = this::class.java.simpleName

    override val userPlan: Flow<UserPlan> = dataStorePrefs.data
        .catch { exception ->
            exception.localizedMessage?.let { Log.e(tag, it) }
            emit(value = emptyPreferences())
        }
        .map { prefs ->
            val premium = prefs[PreferencesKeys.premiumUser] ?: INITIAL_PREMIUM_FLAG
            if (premium) {
                UserPlan.PremiumPlan
            } else {
                val wordCardAdditionAttempts =
                    prefs[PreferencesKeys.wordCardAdditionAttempts] ?: INITIAL_ATTEMPT_COUNT
                val wordCardTranslationAttempts =
                    prefs[PreferencesKeys.wordCardTranslationAttempts] ?: INITIAL_ATTEMPT_COUNT
                UserPlan.FreePlan(
                    wordCardAdditionAttempts = wordCardAdditionAttempts,
                    wordCardTranslationAttempts = wordCardTranslationAttempts
                )
            }
        }

    private suspend fun tryIt(action: suspend () -> Unit) {
        try {
            action()
        } catch (exception: Exception) {
            exception.localizedMessage?.let { Log.e(tag, it) }
        }
    }

    override suspend fun wordCardAdditionUsed() {
        tryIt {
            dataStorePrefs.edit { prefs ->
                if (!(prefs[PreferencesKeys.premiumUser]
                        ?: INITIAL_PREMIUM_FLAG) && (prefs[PreferencesKeys.wordCardAdditionAttempts]
                        ?: INITIAL_ATTEMPT_COUNT) > 0
                ) {
                    prefs[PreferencesKeys.wordCardAdditionAttempts] =
                        (prefs[PreferencesKeys.wordCardAdditionAttempts]
                            ?: INITIAL_ATTEMPT_COUNT) - 1
                }
            }
        }
    }

    override suspend fun wipe() {
        tryIt {
            dataStorePrefs.edit { prefs ->
                prefs[PreferencesKeys.premiumUser] = INITIAL_PREMIUM_FLAG
                prefs[PreferencesKeys.wordCardAdditionAttempts] = INITIAL_ATTEMPT_COUNT
                prefs[PreferencesKeys.wordCardTranslationAttempts] = INITIAL_ATTEMPT_COUNT
            }
        }
    }

    override suspend fun wordCardTranslationUsed() {
        tryIt {
            dataStorePrefs.edit { prefs ->
                if (!(prefs[PreferencesKeys.premiumUser]
                        ?: INITIAL_PREMIUM_FLAG) && (prefs[PreferencesKeys.wordCardTranslationAttempts]
                        ?: INITIAL_ATTEMPT_COUNT) > 0
                ) {
                    prefs[PreferencesKeys.wordCardTranslationAttempts] =
                        (prefs[PreferencesKeys.wordCardTranslationAttempts]
                            ?: INITIAL_ATTEMPT_COUNT) - 1
                }
            }
        }
    }

    override suspend fun premiumPlanActivated() {
        tryIt {
            dataStorePrefs.edit { prefs ->
                prefs[PreferencesKeys.premiumUser] = true
            }
        }
    }

    override suspend fun freePlanActivated() {
        tryIt {
            dataStorePrefs.edit { prefs ->
                prefs[PreferencesKeys.premiumUser] = false
            }
        }
    }

    private object PreferencesKeys {
        val wordCardTranslationAttempts = intPreferencesKey(name = "wordcard_translation_attempts")
        val premiumUser = booleanPreferencesKey(name = "premium_user")
        val wordCardAdditionAttempts = intPreferencesKey(name = "wordcard_addition_attempts")
    }

    companion object {
        const val INITIAL_ATTEMPT_COUNT = 5
        const val INITIAL_PREMIUM_FLAG = false
    }
}