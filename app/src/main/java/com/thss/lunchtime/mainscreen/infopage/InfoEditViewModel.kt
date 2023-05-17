package com.thss.lunchtime.mainscreen.infopage

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thss.lunchtime.component.InfoData
import com.thss.lunchtime.data.userPreferencesStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InfoEditViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(InfoData())
    val uiState = _uiState.asStateFlow()

    fun refresh(context: Context) {
        val userData = context.userPreferencesStore
        viewModelScope.launch {
            _uiState.update { infoData ->
                infoData.copy(
                    ID = userData.data.first().userName,
                )
            }
        }
    }
}