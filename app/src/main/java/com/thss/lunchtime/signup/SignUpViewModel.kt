package com.thss.lunchtime.signup

import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
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

@OptIn(SavedStateHandleSaveableApi::class)
class SignUpViewModel(
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private var _uiState = SavableMutableSaveStateFlow(
        savedStateHandle, "Key", SignUpUser()
    )
    val uiState = _uiState.asStateFlow()

    fun updateUsername(value: String, valid : Boolean, errorMsg: String) {
        _uiState.update { state ->
            state.copy(
                name = value,
                nameErrorMsg = errorMsg,
                isNameValid = valid
            )
        }
        fullValidate()
    }

    fun updateEmail(value: String, valid : Boolean, errorMsg: String) {
        _uiState.update { state ->
            state.copy(
                email = value,
                emailErrorMsg = errorMsg,
                isEmailValid = valid
            )
        }
        fullValidate()
    }

    fun updateEmailValidationCode(value: String, valid : Boolean, errorMsg: String) {
        _uiState.update { state ->
            state.copy(
                emailValidationCode = value,
                emailValidationCodeErrorMsg = errorMsg,
                isEmailValidationCodeValid = valid
            )
        }
        fullValidate()
    }

    fun updatePassword(value: String, valid : Boolean, errorMsg: String) {
        _uiState.update { state ->
            state.copy(
                password = value,
                passwordErrorMsg = errorMsg,
                isPasswordValid = valid
            )
        }
        fullValidate()
    }

    fun updateConfirmPassword(value: String, valid : Boolean, errorMsg: String) {
        _uiState.update { state ->
            state.copy(
                confirmPassword = value,
                confirmPasswordErrorMsg = errorMsg,
                isConfirmPasswordValid = valid
            )
        }
        fullValidate()
    }

    fun inputUsername(value: String) {
        updateUsername(value, true, "")
    }

    fun inputEmail(value: String) {
        if (Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            updateEmail(value, true, "")
        } else {
            updateEmail(value, false, "Invalid Email!")
        }
    }

    fun inputEmailValidationCode(value: String) {
        updateEmailValidationCode(value, true, "")
    }

    fun inputPassword(value: String) {
        val validateMsg = validatePassword(value)
        updatePassword(value,validateMsg == "",  validateMsg)
        inputConfirmPassword(_uiState.value.confirmPassword)
    }

     private fun validatePassword(value: String) : String {
        return if( value.length < 8) {
            "Password length must be longer than 8"
        } else {
            ""
        }
    }

    fun inputConfirmPassword(value: String) {
        val valid = _uiState.value.confirmPassword == _uiState.value.password
        updateConfirmPassword(value, valid ,if (valid) "" else "Two passwords have to be the same")
    }


    private var _isEnabledRegisterButton = mutableStateOf(false)

    val isEnabledRegisterButton = _isEnabledRegisterButton.value

    init { }

    private fun fullValidate() {
        _isEnabledRegisterButton.value =
            _uiState.value.name.isNotEmpty()
                    && _uiState.value.email.isNotEmpty()
                    && _uiState.value.password.isNotEmpty()
                    && _uiState.value.confirmPassword.isNotEmpty()
                    && _uiState.value.emailValidationCode.isNotEmpty()
                    && _uiState.value.isNameValid
                    && _uiState.value.isEmailValid
                    && _uiState.value.isPasswordValid
                    && _uiState.value.isConfirmPasswordValid
                    && _uiState.value.isEmailValidationCodeValid
    }
}