package com.thss.lunchtime.newpost

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NewPostViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(NewPostData())
    val uiState = _uiState.asStateFlow()

    fun changeTitle(value: String) {
        _uiState.update { state ->
            state.copy(title = value)
        }
    }
    fun changeContent(value: String) {
        _uiState.update { state ->
            state.copy(content = value)
        }
    }
}