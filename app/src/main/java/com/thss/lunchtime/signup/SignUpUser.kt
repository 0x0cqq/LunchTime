package com.thss.lunchtime.signup


data class SignUpUser(
    var name: String = "",
    var isNameValid: Boolean = false,
    var nameErrorMsg: String = "",

    var email: String = "",
    var isEmailValid: Boolean = false,
    var emailErrorMsg: String = "",

    var emailValidationCode: String = "",
    var isEmailValidationCodeValid: Boolean = false,
    var emailValidationCodeErrorMsg: String = "",

    var password: String = "",
    var isPasswordValid: Boolean = false,
    var passwordErrorMsg: String = "",

    var confirmPassword: String = "",
    var isConfirmPasswordValid: Boolean = false,
    var confirmPasswordErrorMsg: String = ""
)
