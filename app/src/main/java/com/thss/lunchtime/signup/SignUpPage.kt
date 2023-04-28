package com.thss.lunchtime.signup

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun OutlineTextFieldWithErrorView(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(),
    errorMsg : String = ""
){

    Column {
        OutlinedTextField(
            enabled = enabled,
            readOnly = readOnly,
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            singleLine = singleLine,
            textStyle = textStyle,
            label = label,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxLines = maxLines,
            interactionSource = interactionSource,
            shape = shape,
            colors = colors
        )

        if (isError && errorMsg != ""){
            Text(
                text = errorMsg,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Composable
fun CountdownButton(
    onClick : () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(10.dp),
    content: @Composable (() -> Unit)? = null) {
    var ticks by rememberSaveable { mutableStateOf(0) }

    var isRunning by rememberSaveable { mutableStateOf(false) }


    Button(
        modifier = modifier,
        onClick = { onClick(); ticks = 0; isRunning = true },
        shape = shape,
        enabled = !isRunning
    ) {
        if(isRunning)
            Text(ticks.toString())
        else
            if (content != null) {
                content()
            }
    }
    LaunchedEffect(Unit) {
        while(true) {
            delay(1000.milliseconds)
            if(isRunning) ticks++
            if(ticks >= 60) {
                isRunning = false
            }
        }
    }
}


@Composable
fun SignUpPage(
    onRequestEmailCodeClick: (email: String) -> Unit,
    onSignUpClick: (userData: SignUpUser) -> Unit,
    viewModel: SignUpViewModel
) {
    // 用户名
    val uiState by viewModel.uiState.collectAsState()
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    // 用户名
    LazyColumn (
        modifier = Modifier.padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            Text(
                "Sign Up",
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.titleLarge,
            )
        }
        item {
            OutlineTextFieldWithErrorView(
                value = uiState.name,
                onValueChange = { viewModel.inputUsername(it) },
                label = { Text("Username") },
                modifier = Modifier
                    .fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Outlined.Person, "Username")
                },
                singleLine = true,
                keyboardActions = KeyboardActions {
                    focusManager.moveFocus(FocusDirection.Next)
                },
                isError = !uiState.isNameValid,
                errorMsg = uiState.nameErrorMsg
            )
        }
        // 密码输入栏
        item {
            OutlineTextFieldWithErrorView(
                value = uiState.password,
                onValueChange = { viewModel.inputPassword(it) },
                label = { Text("Password") },
                visualTransformation =
                if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Outlined.Lock, "Password")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                keyboardActions = KeyboardActions {
                    focusManager.moveFocus(FocusDirection.Next)
                },
                isError = !uiState.isPasswordValid,
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { passwordVisible = !passwordVisible }){
                        Icon(imageVector = image, description)
                    }
                },
                errorMsg = uiState.passwordErrorMsg

            )
        }
        // confirm password
        item {
            OutlineTextFieldWithErrorView(
                value = uiState.confirmPassword,
                onValueChange = { viewModel.inputConfirmPassword(it) },
                label = { Text("Confirm Password") },
                visualTransformation =
                if (confirmPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Outlined.Lock, "Password")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                keyboardActions = KeyboardActions {
                    focusManager.moveFocus(FocusDirection.Next)
                },
                isError = !uiState.isConfirmPasswordValid,
                trailingIcon = {
                    val image = if (confirmPasswordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (confirmPasswordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }){
                        Icon(imageVector = image, description)
                    }
                },
                errorMsg = uiState.confirmPasswordErrorMsg

            )
        }

        // email
        item {
            OutlineTextFieldWithErrorView(
                value = uiState.email,
                onValueChange = { viewModel.inputEmail(it) },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Outlined.Email, "Email")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                keyboardActions = KeyboardActions {
                    focusManager.moveFocus(FocusDirection.Next)
                },
                isError = !uiState.isEmailValid,
                errorMsg = uiState.emailErrorMsg
            )
        }
        // email validation code
        item {
            OutlineTextFieldWithErrorView(
                value = uiState.emailValidationCode,
                onValueChange = { viewModel.inputEmailValidationCode(it) },
                label = { Text("Email Validation Code") },
                modifier = Modifier
                    .fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Outlined.Code, "Code")
                },
                trailingIcon = {
                    CountdownButton(
                        onClick = {
                            onRequestEmailCodeClick(uiState.email)
                        },
                        modifier = Modifier.padding(end = 5.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Send")
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                keyboardActions = KeyboardActions {
                    focusManager.moveFocus(FocusDirection.Next)
                },
                isError = !uiState.isEmailValidationCodeValid,
                errorMsg = uiState.emailValidationCodeErrorMsg
            )
        }
        item {
            Button(
                onClick = {
                    onSignUpClick(uiState)
                },
                modifier = Modifier.padding(10.dp),
                shape = RoundedCornerShape(10.dp),
                enabled = viewModel.isEnabledRegisterButton
            ) {
                Text(
                    "Sign Up"
                )
            }
        }
    }
}
