package com.olegaches.engcards.fragment.new_wordcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olegaches.engcards.domain.model.WordCard
import com.olegaches.engcards.domain.use_case.CreateWordCardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewWordCardViewModel @Inject constructor(
    private val createWordCardUseCase: CreateWordCardUseCase,
) : ViewModel() {
    private val _wordState = MutableStateFlow("")
    val wordState = _wordState.asStateFlow()

    private val _nativeTranslationState = MutableStateFlow("")
    val nativeTranslationState = _nativeTranslationState.asStateFlow()

    fun onWordChanged(word: String) {
        _wordState.update { word }
    }

    fun onNativeTranslationChanged(nativeTranslation: String) {
        _nativeTranslationState.update { nativeTranslation }
    }

    fun createWordCard() {
        viewModelScope.launch {
            createWordCardUseCase(
                WordCard(
                    word = wordState.value,
                    nativeTranslation = nativeTranslationState.value,
                    id = 0
                )
            )
        }
    }
}
