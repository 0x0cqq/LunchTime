package com.thss.lunchtime.common

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// https://medium.com/mobile-app-development-publication/saving-stateflow-state-in-viewmodel-2ee9ed9b1a83
class SavableMutableSaveStateFlow<T>(
    private val savedStateHandle: SavedStateHandle,
    private val key: String,
    defaultValue: T
) {
    private val _state: MutableStateFlow<T> =
        MutableStateFlow(
            savedStateHandle.get<T>(key) ?: defaultValue)

    var value: T
        get() = _state.value
        set(value) {
            _state.value = value
            savedStateHandle[key] = value
        }

    fun update(function: (T) -> T) {
        while (true) {
            val prevValue = value
            val nextValue = function(prevValue)
            if (_state.compareAndSet(prevValue, nextValue)) {
                return
            }
        }
    }

    fun asStateFlow(): StateFlow<T> = _state
}