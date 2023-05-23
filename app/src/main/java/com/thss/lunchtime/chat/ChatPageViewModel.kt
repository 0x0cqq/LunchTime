package com.thss.lunchtime.chat

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class ChatPageViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ChatPageState())
    val uiState = _uiState

    fun updateInputValue(value: String) {
        _uiState.value = _uiState.value.copy(inputValue = value)
    }
}