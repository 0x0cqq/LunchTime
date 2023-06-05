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
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thss.lunchtime.chat.ChatPage
import com.thss.lunchtime.chat.ChatPageViewModel
import com.thss.lunchtime.data.userPreferencesStore
import com.thss.lunchtime.info.OtherInfoPage
import com.thss.lunchtime.info.OtherInfoPageViewModel
import com.thss.lunchtime.listpages.BlockListPage
import com.thss.lunchtime.listpages.FansListPage
import com.thss.lunchtime.listpages.FollowingListPage
import com.thss.lunchtime.listpages.StarPostListPage
import com.thss.lunchtime.mainscreen.MainScreen
import com.thss.lunchtime.mainscreen.MainScreenViewModel
import com.thss.lunchtime.mainscreen.infopage.InfoEditPage
import com.thss.lunchtime.mainscreen.infopage.InfoEditViewModel
import com.thss.lunchtime.mainscreen.infopage.MyInfoPage
import com.thss.lunchtime.mainscreen.infopage.MyInfoPageViewModel
import com.thss.lunchtime.mediaplayer.ImagePlayPage
import com.thss.lunchtime.mediaplayer.VideoPlayPage
import com.thss.lunchtime.network.LunchTimeApi
import com.thss.lunchtime.newpost.NewPostPage
import com.thss.lunchtime.newpost.NewPostViewModel
import com.thss.lunchtime.post.PostDetailPage
import com.thss.lunchtime.post.PostDetailViewModel
import com.thss.lunchtime.search.SearchPage
import com.thss.lunchtime.search.SearchPageViewModel
import com.thss.lunchtime.signup.SignUpPage
import com.thss.lunchtime.signup.SignUpViewModel
import com.thss.lunchtime.ui.theme.LunchTimeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import me.onebone.parvenu.ParvenuString
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
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
    val searchPageViewModel : SearchPageViewModel = viewModel()
    val applicationNavController = rememberNavController()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userData = context.userPreferencesStore

    val json = Json { ignoreUnknownKeys = true }
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
                                applicationNavController.navigate(
                                    "main",
                                    NavOptions.Builder().setPopUpTo("login", true).build()
                                )
                                userData.updateData { userData ->
                                    userData.toBuilder().setUserName(state.name).setIsLogin(true).build()
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
                            if (state.title.isEmpty()) {
                                Toast.makeText(
                                    context, "标题不能为空",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@launch
                            }
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

                            val videos = state.selectedVideoUris.mapIndexed { index, it ->
                                val requestBody = it.asRequestBody("video/*".toMediaType())
                                MultipartBody.Part.createFormData(
                                    "video",
                                    it.name,
                                    requestBody
                                )
                            }

                            val userName = userData.data.first().userName
                            Log.d("LunchTime", "Current Username:$userName")
                            val richContentString = json.encodeToString(ParvenuString.serializer(), state.richContent.parvenuString)
                            val response = LunchTimeApi.retrofitService.post(
                                userName.toRequestBody("text/plain".toMediaType()),
                                state.title.toRequestBody("text/plain".toMediaType()),
                                richContentString.toRequestBody("text/plain".toMediaType()),
                                state.location.toRequestBody("text/plain".toMediaType()),
                                state.tag.toRequestBody("text/plain".toMediaType()),
                                images,
                                videos
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
                                newPostViewModel.clear()
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
        composable("search"){
            SearchPage(
                onClickPostPreviewCard = { postId ->
                    applicationNavController.navigate("post/$postId")
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
                searchPageViewModel = searchPageViewModel
            )
        }
        composable("main") {
            MainScreen(
                onClickSearch = {
                    applicationNavController.navigate("search")
                },
                onOpenChat = { name ->
                    applicationNavController.navigate("chat/$name")
                },
                onOpenInfoEdit = {
                    applicationNavController.navigate("editProfile")
                },
                onNewPost = {
                    applicationNavController.navigate("newpost")
                },
                onOpenPost = { postId ->
                    applicationNavController.navigate("post/$postId")
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
                onOpenFollows = {scope.launch {
                    val userName = userData.data.first().userName
                    applicationNavController.navigate("followingList/$userName")
                }
                },
                onOpenFans = { scope.launch{
                    val userName = userData.data.first().userName
                    applicationNavController.navigate("fansList/$userName")
                }
                },
                onOpenSaved = { scope.launch{
                    val userName = userData.data.first().userName
                    applicationNavController.navigate("starPostList/$userName")
                }
                },
                onClickMedia = { url, isVideo ->
                    val modifiedUrl = url.replace("/", "!")
                    if(isVideo){
                        applicationNavController.navigate("videoPage/$modifiedUrl")
                    } else{
                        applicationNavController.navigate("imagePage/$modifiedUrl")
                    }
                }
                ,
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
                onOpenMedia = {  url, isVideo ->
                    val modifiedUrl = url.replace("/", "!")
                    if(isVideo){
                        applicationNavController.navigate("videoPage/$modifiedUrl")
                    } else{
                        applicationNavController.navigate("imagePage/$modifiedUrl")
                    }
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
                onOpenBlockList = { scope.launch{
                    val userName = userData.data.first().userName
                    applicationNavController.navigate("blockList/$userName")
                } },
                infoEditViewModel = infoEditViewModel
            )
        }
        composable("otherInfoPage/{userName}"){ backStackEntry ->
            val userName : String = backStackEntry.arguments?.getString("userName")!!
            OtherInfoPage(
                onClickBack = { applicationNavController.popBackStack() },
                otherInfoPageViewModel = otherInfoPageViewModel,
                userName = userName,
                onClickPost = { postId ->
                    applicationNavController.navigate("post/$postId") },
                onClickFans = { applicationNavController.navigate("fansList/$userName") },
                onClickFollows = { applicationNavController.navigate("followingList/$userName") },
                onClickSaved = { applicationNavController.navigate("starPostList/$userName") },
                onClickChat = { name ->
                    applicationNavController.navigate("chat/$name")
                }
            )
        }
        composable("myInfoPage"){
            MyInfoPage(
                onOpenInfoEdit = { applicationNavController.navigate("editProfile") },
                onClickPost = { postId ->
                    applicationNavController.navigate("post/$postId") },
                onOpenFansList = { scope.launch{
                        val userName = userData.data.first().userName
                        applicationNavController.navigate("fansList/$userName")
                    }
                },
                onOpenFollowingList = {scope.launch {
                        val userName = userData.data.first().userName
                        applicationNavController.navigate("followingList/$userName")
                    }
                },
                onOpenSavedList = {scope.launch {
                    val userName = userData.data.first().userName
                    applicationNavController.navigate("starPostList/$userName")
                }
                },
                myInfoPageViewModel = myInfoPageViewModel,
            )
        }
        composable("chat/{userName}") { backStackEntry ->
            val chatViewModel : ChatPageViewModel = viewModel()
            val userName : String = backStackEntry.arguments?.getString("userName")!!
            ChatPage(
                onBack = {
                    applicationNavController.popBackStack()
                },
                oppoSiteUserName = userName,
                chatPageViewModel = chatViewModel
            )
        }
        composable("blockList/{userName}"){ backStackEntry ->
            val userName : String = backStackEntry.arguments?.getString("userName")!!
            BlockListPage(
                onBack = { applicationNavController.popBackStack() },
                userName = userName,
                onClickUserInfo = { targetUserName -> applicationNavController.navigate("otherInfoPage/$targetUserName") }
            )
        }
        composable("followingList/{userName}"){backStackEntry ->
            val userName : String = backStackEntry.arguments?.getString("userName")!!
            FollowingListPage(
                onBack = { applicationNavController.popBackStack() },
                userName = userName,
                onClickUserInfo = { targetUserName -> applicationNavController.navigate("otherInfoPage/$targetUserName") }
            )
        }
        composable("fansList/{userName}"){backStackEntry ->
            val userName : String = backStackEntry.arguments?.getString("userName")!!
            FansListPage(
                onBack = { applicationNavController.popBackStack() },
                userName = userName,
                onClickUserInfo = { targetUserName -> applicationNavController.navigate("otherInfoPage/$targetUserName") }
            )
        }
        composable("starPostList/{userName}"){backStackEntry ->
            val userName : String = backStackEntry.arguments?.getString("userName")!!
            StarPostListPage(
                onBack = { applicationNavController.popBackStack() },
                onOpenUserInfo = { targetUserName ->
                    scope.launch {
                        val myUserName = userData.data.first().userName
                        if (myUserName == targetUserName){
                            applicationNavController.navigate("myInfoPage")
                        }
                        else{
                            applicationNavController.navigate("otherInfoPage/$targetUserName")
                        }
                    } },
                onOpenPost = {postId ->
                    applicationNavController.navigate("post/$postId")
                },
                userName = userName)
        }
        composable("videoPage/{url}"){backStackEntry ->
            val url : String = backStackEntry.arguments?.getString("url")!!
            val modifiedUrl = url.replace("!","/")
            VideoPlayPage(url = modifiedUrl, onBack = { applicationNavController.popBackStack() })
        }
        composable("imagePage/{url}"){backStackEntry ->
            val url : String = backStackEntry.arguments?.getString("url")!!
            val modifiedUrl = url.replace("!","/")
            ImagePlayPage(url = modifiedUrl, onBack = { applicationNavController.popBackStack() })
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