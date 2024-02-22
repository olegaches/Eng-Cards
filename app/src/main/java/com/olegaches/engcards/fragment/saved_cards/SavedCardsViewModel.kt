package com.olegaches.engcards.fragment.saved_cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olegaches.engcards.domain.use_case.GetSavedCardsUseCase
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
    private val getSavedCardsUseCase: GetSavedCardsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(SavedCardsState())
    val state = _state.asStateFlow()

    init {
        loadCards()
    }

    private var searchJob: Job? = null

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

    fun loadCards() {
        val state = _state
        state.update { it.copy(loading = true) }
        viewModelScope.launch {
            getSavedCardsUseCase().collect { cards ->
                state.update { it.copy(cards = cards, loading = false) }
            }
        }
    }
}
