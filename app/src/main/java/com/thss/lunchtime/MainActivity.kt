package com.thss.lunchtime

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thss.lunchtime.mainscreen.MainScreen
import com.thss.lunchtime.mainscreen.MainScreenViewModel
import com.thss.lunchtime.network.LunchTimeApi
import com.thss.lunchtime.newpost.NewPostPage
import com.thss.lunchtime.signup.SignUpPage
import com.thss.lunchtime.signup.SignUpViewModel

import com.thss.lunchtime.ui.theme.LunchTimeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.R)
class MainActivity : ComponentActivity() {
    val mainScreenViewModel : MainScreenViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.setDecorFitsSystemWindows(false)
//        } else {
//            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
//        }
        super.onCreate(savedInstanceState)
        setContent {
            LunchTimeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    LoginPage({ _, _ -> }, { _, _ -> })
//                    SignUpPage({},{}, signupViewModel)
//                    MainScreen(mainScreenViewModel)
                    Application()
                }
            }
        }
    }
}


@Composable
fun Application(modifier: Modifier = Modifier) {
    val signupViewModel : SignUpViewModel = viewModel()
    val mainScreenViewModel : MainScreenViewModel = viewModel()
    val applicationNavController = rememberNavController()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    NavHost(
        navController = applicationNavController,
        startDestination = "main"
    ) {
        composable("login") {
            LoginPage(
                onClickLogin = { name, password ->
                    scope.launch {
                        try {
                            val response = LunchTimeApi.retrofitService.login(name, password)
                            val message = if( response.status ){
                                "登录成功"
                            } else {
                                "登录失败, " + response.message
                            }
                            Toast.makeText(
                                context, message,
                                8.coerceAtLeast(message.length)
                            ).show()
                            delay(1000)
                            applicationNavController.navigate("main", NavOptions.Builder().setPopUpTo("login", true).build())
                        } catch ( e : Exception) {
                            Log.e("LunchTime", e.toString())
                            Toast.makeText(
                                context, "网络错误",
                                8.coerceAtLeast("网络错误".length)
                            ).show()
                        }
                    }
                },
                onClickSignup = {
                    applicationNavController.navigate("register")
                }
            )
        }
        composable("register") {
            SignUpPage(
                onRequestEmailCodeClick = { address ->
                    scope.launch {
                        try {
                            val response =
                                LunchTimeApi.retrofitService.getEmailVerificationCode(address)
                            val message = if( response.status ){
                                "验证码已发送"
                            } else {
                                "验证码发送失败, " + response.message
                            }
                            Toast.makeText(
                                context, message,
                                8.coerceAtLeast(message.length)
                            ).show()
                        } catch ( e : Exception) {
                            Log.e("LunchTime", e.toString())
                            Toast.makeText(
                                context, "网络错误",
                                8.coerceAtLeast("网络错误".length)
                            ).show()
                        }
                    }
                },
                onSignUpClick = { state ->
                    scope.launch {
                        try {
                            val response =
                                LunchTimeApi.retrofitService.register(
                                    state.name,
                                    state.password,
                                    state.email,
                                    state.emailValidationCode)
                            val message = if( response.status ){
                                "注册成功！"
                            } else {
                                "注册失败," + response.message
                            }
                            Toast.makeText(
                                context, message,
                                8.coerceAtLeast(message.length)
                            ).show()
                            delay(1000)
                            applicationNavController.navigate("login")
                        } catch ( e : Exception) {
                            Log.e("LunchTime", e.toString())
                            Toast.makeText(
                                context, "网络错误",
                                8.coerceAtLeast("网络错误".length)
                            ).show()
                        }
                    }
                },
                signupViewModel
            )
        }
        composable("newpost") {
            NewPostPage(
                onClickBack = {
                    applicationNavController.popBackStack()
                }
            )
        }
        composable("main") {
            MainScreen(
                onNewPost = {
                    applicationNavController.navigate("newpost")
                },
                mainScreenViewModel
            )
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LunchTimeTheme {
        Greeting("Android")
    }
}