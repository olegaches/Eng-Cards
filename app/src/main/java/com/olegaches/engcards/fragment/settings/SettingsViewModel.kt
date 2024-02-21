package com.olegaches.engcards.fragment.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olegaches.engcards.domain.use_case.WipeSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val wipeSettingsUseCase: WipeSettingsUseCase
) : ViewModel() {

    fun wipeSettings() {
        viewModelScope.launch {
            wipeSettingsUseCase()
        }
    }
}