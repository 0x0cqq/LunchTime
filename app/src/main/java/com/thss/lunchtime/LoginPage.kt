package com.thss.lunchtime

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thss.lunchtime.data.userPreferencesStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LoginPage(onAlreadyLogin: () -> Unit, onClickLogin : (username: String, password: String) -> Unit,
              onClickSignup: () -> Unit) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        scope.launch {
            if(context.userPreferencesStore.data.first().isLogin) {
                onAlreadyLogin()
            }
        }
    }


    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .imeNestedScroll(),
    ) {
        if (maxWidth > maxHeight) { // 横屏
            Row (
                verticalAlignment = Alignment.CenterVertically,
            ){
                LoginTitle()
                LoginPanel(onClickLogin, onClickSignup)
            }
        } else { //竖屏
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                LoginTitle()
                LoginPanel(onClickLogin, onClickSignup)
            }
        }
    }
}

@Composable
fun LoginTitle(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.title),
        contentDescription = "Title Image",
        modifier = modifier.fillMaxWidth(0.5f)
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPanel(onClickLogin : (username: String, password: String) -> Unit,
              onClickSignup: () -> Unit) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 用户名栏
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            leadingIcon = {
                Icon(Icons.Outlined.Person,"Username")
            },
            singleLine = true,
            keyboardActions = KeyboardActions {
                focusManager.moveFocus(FocusDirection.Next)
            },
        )
        // 密码输入栏
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation =
                if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            leadingIcon = {
                Icon(Icons.Outlined.Lock, "Password")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            keyboardActions = KeyboardActions {
                focusManager.clearFocus()
            },
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }){
                    Icon(imageVector = image, description)
                }
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 登录和注册按钮
            Button(
                onClick = { onClickLogin(username, password) },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.padding(10.dp)
                    .weight(1f)) {
                Text("Log In")
            }

            OutlinedButton(
                onClick = { onClickSignup() },
                border = BorderStroke(1.dp, Color.Transparent),
                modifier = Modifier
                    .padding(10.dp)
                    .weight(1f)) {
                Text("Sign Up", textDecoration = TextDecoration.Underline)
            }
        }
    }
}

@Preview
@Composable
fun LoginPagePreview() {
    LoginPage(onAlreadyLogin = {}, onClickLogin = { _, _ -> }, onClickSignup = { })
}