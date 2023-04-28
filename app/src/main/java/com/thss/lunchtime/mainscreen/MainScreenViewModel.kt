package com.thss.lunchtime.mainscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainScreenViewModel : ViewModel() {
    var selectItem by mutableStateOf(0)
        private set
    fun selectNavItem(index : Int) {
        selectItem = index
    }
}