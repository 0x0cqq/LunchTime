package com.thss.lunchtime

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thss.lunchtime.data.userPreferencesStore
import com.thss.lunchtime.info.OtherInfoPageViewModel
import com.thss.lunchtime.mainscreen.MainScreen
import com.thss.lunchtime.mainscreen.MainScreenViewModel
import com.thss.lunchtime.mainscreen.infopage.InfoEditPage
import com.thss.lunchtime.mainscreen.infopage.InfoEditViewModel
import com.thss.lunchtime.mainscreen.infopage.MyInfoPage
import com.thss.lunchtime.mainscreen.infopage.MyInfoPageViewModel
import com.thss.lunchtime.network.LunchTimeApi
import com.thss.lunchtime.newpost.NewPostPage
import com.thss.lunchtime.newpost.NewPostViewModel
import com.thss.lunchtime.post.PostDetailPage
import com.thss.lunchtime.post.PostDetailViewModel
import com.thss.lunchtime.signup.SignUpPage
import com.thss.lunchtime.signup.SignUpViewModel
import com.thss.lunchtime.ui.theme.LunchTimeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.security.MessageDigest

@RequiresApi(Build.VERSION_CODES.R)
class MainActivity : ComponentActivity() {
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
    val newPostViewModel : NewPostViewModel = viewModel()
    val infoEditViewModel : InfoEditViewModel = viewModel()
    val otherInfoPageViewModel : OtherInfoPageViewModel = viewModel()
    val myInfoPageViewModel : MyInfoPageViewModel = viewModel()
    val applicationNavController = rememberNavController()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userData = context.userPreferencesStore

    NavHost(
        navController = applicationNavController,
        startDestination = "login",
        modifier = modifier
    ) {
        composable("login") {
            LoginPage(
                onAlreadyLogin = {
                    applicationNavController.navigate(
                        "main",
                        NavOptions.Builder().setPopUpTo("login", true).build()
                    )
                    scope.launch {
                        val userName = userData.data.first().userName
                        Toast.makeText(context, "Welcome Back, $userName" , Toast.LENGTH_SHORT).show()
                    }
                },
                onClickLogin = { name, password ->
                    scope.launch {
                        try {
                            Log.d("Lunchtime", password)

                            val passwordHashed = Base64.encodeToString(MessageDigest.getInstance("SHA-256").digest(password.toByteArray()), Base64.DEFAULT)
                            Log.d("Lunchtime", passwordHashed)

                            val response = LunchTimeApi.retrofitService.login(
                                name = name,
                                password = passwordHashed
                            )

                            val message = if( response.status ){
                                "登录成功"
                            } else {
                                "登录失败, " + response.message
                            }
                            Toast.makeText(
                                context, message,
                                Toast.LENGTH_SHORT
                            ).show()
                            delay(1000)
                            if(response.status) {
                                applicationNavController.navigate(
                                    "main",
                                    NavOptions.Builder().setPopUpTo("login", true).build()
                                )
                                userData.updateData { userData ->
                                    userData.toBuilder().setUserName(name).setIsLogin(true).build()
                                }
                            }
                        } catch ( e : Exception) {
                            Log.e("LunchTime", e.toString())
                            Toast.makeText(
                                context, "网络错误",
                                Toast.LENGTH_SHORT
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
                onBackClick = {
                    applicationNavController.popBackStack()
                },
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
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch ( e : Exception) {
                            Log.e("LunchTime", e.toString())
                            Toast.makeText(
                                context, "网络错误",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                onSignUpClick = { state ->
                    scope.launch {
                        try {
                            val passwordHashed = Base64.encodeToString(MessageDigest.getInstance("SHA-256").digest(state.password.toByteArray()), Base64.DEFAULT)

                            val response =
                                LunchTimeApi.retrofitService.register(
                                    state.name,
                                    passwordHashed,
                                    state.email,
                                    state.emailValidationCode)
                            val message = if( response.status ){
                                "注册成功！"
                            } else {
                                "注册失败," + response.message
                            }
                            Toast.makeText(
                                context, message,
                                Toast.LENGTH_SHORT
                            ).show()
                            delay(1000)
                            if( response.status ) {
                                // back to login
                                applicationNavController.popBackStack()
                            }
                        } catch ( e : Exception) {
                            Log.e("LunchTime", e.toString())
                            Toast.makeText(
                                context, "网络错误",
                                Toast.LENGTH_SHORT
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
                },
                onClickSend = { state ->
                    scope.launch {
                        try {
                            val images = state.selectedImgUris.mapIndexed { index, it ->
                                val stream = ByteArrayOutputStream()
                                it.asAndroidBitmap()
                                    .compress(Bitmap.CompressFormat.JPEG, 100, stream)
                                val requestBody = stream.toByteArray()
                                    .toRequestBody(
                                        "image/*".toMediaType()
                                    )
                                MultipartBody.Part.createFormData(
                                    "picture",
                                    "uploaded-$index.jpeg",
                                    requestBody
                                )
                            }

                            val userName = userData.data.first().userName
                            Log.d("LunchTime", "Current Username:$userName")
                            val response = LunchTimeApi.retrofitService.post(
                                userName.toRequestBody("text/plain".toMediaType()),
                                state.title.toRequestBody("text/plain".toMediaType()),
                                state.content.toRequestBody("text/plain".toMediaType()),
                                state.location.toRequestBody("text/plain".toMediaType()),
                                state.tag.toRequestBody("text/plain".toMediaType()),
                                images
                            )

                            val message = if( response.status ){
                                "发布成功, postID: " + response.postId
                            } else {
                                "发布失败," + response.message
                            }
                            Toast.makeText(
                                context, message,
                                Toast.LENGTH_SHORT
                            ).show()
                            delay(1000)
                            if( response.status ) {
                                applicationNavController.navigate("main")
                            }
                        } catch ( e : Exception) {
                            Log.e("LunchTime", e.toString())
                            Toast.makeText(
                                context, "网络错误",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                newPostViewModel
            )
        }
        composable("main") {
            MainScreen(
                onOpenInfoEdit = {
                    applicationNavController.navigate("editProfile")
                },
                onOpenPost = { postId ->
                    applicationNavController.navigate("post/$postId")
                },
                onNewPost = {
                    applicationNavController.navigate("newpost")
                },
                onOpenUserInfo = { targetUserName ->
                    scope.launch {
                        val myUserName = userData.data.first().userName
                        if (myUserName == targetUserName){
                            applicationNavController.navigate("myInfoPage")
                        }
                        else{
                            applicationNavController.navigate("otherInfoPage/$targetUserName")
                        }
                    }
                },
                mainScreenViewModel = mainScreenViewModel
            )
        }
        composable("post/{postId}") { backStackEntry ->
            val postId : Int = backStackEntry.arguments?.getString("postId")!!.toInt()
            val postDetailViewModel : PostDetailViewModel = viewModel()
            PostDetailPage(
                onBack = {
                    applicationNavController.popBackStack()
                },
                onOpenUserInfo = { targetUserName ->
                    scope.launch {
                        val myUserName = userData.data.first().userName
                        if (myUserName == targetUserName){
                            applicationNavController.navigate("myInfoPage")
                        }
                        else{
                            applicationNavController.navigate("otherInfoPage/$targetUserName")
                        }
                    }
                },
                postID = postId,
                postDetailViewModel = postDetailViewModel
            )
        }
        composable("editProfile") {
            InfoEditPage(
                onBack = {
                    applicationNavController.popBackStack()
                },
                onLogOut = {
                    scope.launch {
                        userData.updateData { userData ->
                            userData.toBuilder().setUserName("").setIsLogin(false).build()
                        }
                        while (applicationNavController.popBackStack()) {}
                        applicationNavController.navigate("login")
                    }
                },
                infoEditViewModel = infoEditViewModel
            )
        }
        composable("otherInfoPage/{userName}"){ backStackEntry ->
            val userName : String = backStackEntry.arguments?.getString("userName")!!
            OtherInfoPage(
                otherInfoPageViewModel = otherInfoPageViewModel,
                userName = userName
            )
        }
        composable("myInfoPage"){
            MyInfoPage(
                onOpenInfoEdit = { applicationNavController.navigate("editProfile") },
                onClickPost = { postId ->
                    applicationNavController.navigate("post/$postId") },
                myInfoPageViewModel = myInfoPageViewModel,
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