package com.olegaches.engcards.fragment.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olegaches.engcards.domain.model.UserPlan
import com.olegaches.engcards.domain.use_case.ActivatePremiumUseCase
import com.olegaches.engcards.domain.use_case.GetUserPlanStreamUseCase
import com.olegaches.engcards.domain.use_case.GetWordCardRandomListUseCase
import com.olegaches.engcards.domain.use_case.UseWordCardTranslationAttemptUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWordCardRandomListUseCase: GetWordCardRandomListUseCase,
    private val getUserPlanStreamUseCase: GetUserPlanStreamUseCase,
    private val useWordCardTranslationAttemptUseCase: UseWordCardTranslationAttemptUseCase,
    private val activatePremiumUseCase: ActivatePremiumUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())

    val state = _state.asStateFlow()

    init {
        observeUserPlan()
        loadCards()
    }

    private fun observeUserPlan() {
        viewModelScope.launch {
            getUserPlanStreamUseCase().collect { userPlan ->
                val attempts = if (userPlan is UserPlan.FreePlan) {
                    userPlan.wordCardTranslationAttempts
                } else {
                    Int.MAX_VALUE
                }
                _state.update {
                    it.copy(translationAttempts = attempts)
                }
            }
        }
    }

    private fun loadCards() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            getWordCardRandomListUseCase(5).collect { cards ->
                _state.update { it.copy(cards = cards, loading = false) }
            }
        }
    }

    fun onPlayAgainClicked() {
        loadCards()
    }

    fun dismissPremiumAd() {
        _state.update { it.copy(showPremiumAd = false) }
    }

    fun showPremiumAd() {
        _state.update { it.copy(showPremiumAd = true) }
    }

    fun onActivatePremium() {
        viewModelScope.launch {
            activatePremiumUseCase()
            dismissPremiumAd()
        }
    }

    fun onRequireTranslation(): Boolean {
        val translationAttempts = _state.value.translationAttempts
        viewModelScope.launch {
            if (translationAttempts <= 0) {
                _state.update { it.copy(showPremiumAd = true) }
            }
            useWordCardTranslationAttemptUseCase()
        }
        return translationAttempts >= 1
    }
}