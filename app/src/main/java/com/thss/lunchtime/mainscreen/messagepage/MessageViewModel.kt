package com.thss.lunchtime.mainscreen.messagepage

import androidx.lifecycle.ViewModel
import com.thss.lunchtime.mainscreen.homepage.HomepageState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MessageViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MessagepageState())
    val uiState = _uiState.asStateFlow()

    fun selectTab(index: Int) {
        _uiState.update { state ->
            state.copy(selectedIndex = index)
        }
    }

    fun refresh() {
        // TODO
    }
}