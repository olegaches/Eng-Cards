package com.olegaches.engcards.fragment.saved_cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olegaches.engcards.domain.model.UserPlan
import com.olegaches.engcards.domain.use_case.ActivatePremiumUseCase
import com.olegaches.engcards.domain.use_case.GetSavedCardsUseCase
import com.olegaches.engcards.domain.use_case.GetUserPlanStreamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedCardsViewModel @Inject constructor(
    private val getSavedCardsUseCase: GetSavedCardsUseCase,
    private val activatePremiumUseCase: ActivatePremiumUseCase,
    private val getUserPlanStreamUseCase: GetUserPlanStreamUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(SavedCardsState())
    val state = _state.asStateFlow()

    init {
        observeUserPlan()
        loadCards()
    }

    private fun observeUserPlan() {
        viewModelScope.launch {
            getUserPlanStreamUseCase().collect { userPlan ->
                val attempts = if (userPlan is UserPlan.FreePlan) {
                    userPlan.wordCardAdditionAttempts
                } else {
                    Int.MAX_VALUE
                }
                _state.update {
                    it.copy(additionAttempts = attempts)
                }
            }
        }
    }

    private var searchJob: Job? = null

    fun onRequireTranslation(): Boolean {
        val additionAttempts = _state.value.additionAttempts
        if (additionAttempts <= 0) {
            _state.update { it.copy(showPremiumAd = true) }
        }
        return additionAttempts >= 1
    }

    fun dismissPremiumAd() {
        _state.update { it.copy(showPremiumAd = false) }
    }

    fun onActivatePremium() {
        viewModelScope.launch {
            activatePremiumUseCase()
            dismissPremiumAd()
        }
    }

    fun onQueryChanged(query: String) {
        searchJob?.cancel()
        val state = _state
        state.update { it.copy(loading = true, query = query) }
        searchJob = viewModelScope.launch {
            delay(400L)
            getSavedCardsUseCase(query).collect { cards ->
                state.update { it.copy(loading = false, cards = cards) }
            }
        }
    }

    fun onQueryCleared() {
        viewModelScope.launch {
            val state = _state
            state.update { it.copy(loading = true, query = "") }
            getSavedCardsUseCase().collect { cards ->
                state.update { it.copy(loading = false, cards = cards) }
            }
        }
    }

    private fun loadCards() {
        val state = _state
        state.update { it.copy(loading = true) }
        viewModelScope.launch {
            getSavedCardsUseCase().collect { cards ->
                state.update { it.copy(cards = cards, loading = false) }
            }
        }
    }
}
