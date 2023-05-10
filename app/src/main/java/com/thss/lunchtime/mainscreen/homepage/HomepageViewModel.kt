package com.thss.lunchtime.mainscreen.homepage

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.thss.lunchtime.PostData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class HomepageViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomepageState())
    val uiState = _uiState.asStateFlow()

    fun selectTab(index: Int) {
        _uiState.update { state ->
            state.copy(selectedIndex = index)
        }
    }
    fun addRandomPost() {
        _uiState.update { state ->
            state.copy(postDataList = state.postDataList + PostData(title = "test", content = Random.nextInt().toString()))
        }
    }
}