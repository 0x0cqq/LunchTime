package com.thss.lunchtime.signup

import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import com.thss.lunchtime.common.SavableMutableSaveStateFlow



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
        val valid = value == _uiState.value.password
        updateConfirmPassword(value, valid ,if (valid) "" else "Two passwords have to be the same")
    }


    var isEnabledRegisterButton = mutableStateOf(false)
        private set

    init { }

    private fun fullValidate() {
        isEnabledRegisterButton.value =
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