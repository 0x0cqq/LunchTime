package com.thss.lunchtime.mainscreen.infopage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.thss.lunchtime.component.InfoData
import com.thss.lunchtime.data.userPreferencesStore
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream


// 除了注销，其他（修改密码blabla）的应该都可以在这里再开一个 navigation 解决？
// 就不要往上走了 上面好拥挤
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun InfoEditPage(onBack: () -> Unit, onLogOut: () -> Unit, onOpenBlockList: () -> Unit, infoEditViewModel: InfoEditViewModel) {
    val infoData = infoEditViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if(uri != null) {
            val inputStream = context.contentResolver.openInputStream(uri)
            val imageBitmap = BitmapFactory.decodeStream(inputStream).asImageBitmap()
            val stream = ByteArrayOutputStream()
            imageBitmap.asAndroidBitmap()
                .compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val requestBody = stream.toByteArray()
                .toRequestBody(
                    "image/*".toMediaType()
                )
            val image = listOf(MultipartBody.Part.createFormData(
                "image",
                "uploaded.jpeg",
                requestBody
            ))
            infoEditViewModel.modifyUserImage(context,image)
        }
    }

    LaunchedEffect(Unit) {
        infoEditViewModel.refresh(context)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                },
                title = {
                    Text(text = "个人信息详情")
                }
            )
        }
    ) { paddingValues -> Column(
        modifier = Modifier
            .padding(paddingValues)
    ) {
        Column (
            modifier = Modifier
            .fillMaxWidth()
        ) {
            val openPasswordDialog = remember { mutableStateOf(false) }
            val originPassword = remember {
                mutableStateOf("")
            }
            val newPassword = remember {
                mutableStateOf("")
            }
            val originPasswordHidden = remember { mutableStateOf(true) }
            val newPasswordHidden = remember { mutableStateOf(true) }


            ImageChange(
                avatar = infoData.value.Avatar,
                onNewImage = {
                    launcher.launch (
                        PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                })
            SimpleInfoChange(onOpenBlockList ,infoData.value, infoEditViewModel, context)

            Spacer(modifier = Modifier.height(50.dp))

            // 修改密码
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                onClick = { openPasswordDialog.value = !openPasswordDialog.value }
            ) {
                Text(text = "修改密码", fontSize = 18.sp)
            }

            if (openPasswordDialog.value) {
                AlertDialog(
                    onDismissRequest = {
                        // 当用户点击对话框以外的地方或者按下系统返回键将会执行的代码
                        openPasswordDialog.value = false
                    },
                    title = {
                        Text(
                            text = "修改用户名",
                            fontWeight = FontWeight.W700,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    },
                    text = {
                        Column() {
                            TextField(
                                value = originPassword.value,
                                onValueChange = {
                                    originPassword.value = it
                                },
                                singleLine = true,
                                label = {
                                    Text("原密码")
                                },
                                trailingIcon = {
                                    IconButton(
                                        onClick = {
                                            originPasswordHidden.value = !originPasswordHidden.value
                                        }
                                    ){
                                        Icon(Icons.Default.Visibility, contentDescription = null)
                                    }
                                },
                                visualTransformation = if(originPasswordHidden.value) PasswordVisualTransformation() else VisualTransformation.None
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            TextField(
                                value = newPassword.value,
                                onValueChange = {
                                    newPassword.value = it
                                },
                                singleLine = true,
                                label = {
                                    Text(text = "新密码")
                                },
                                trailingIcon = {
                                    IconButton(
                                        onClick = {
                                            newPasswordHidden.value = !newPasswordHidden.value
                                        }
                                    ){
                                        Icon(Icons.Default.Visibility, contentDescription = null)
                                    }
                                },
                                visualTransformation = if(newPasswordHidden.value) PasswordVisualTransformation() else VisualTransformation.None
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                infoEditViewModel.modifyPassword(context, originPassword.value, newPassword.value)
                                newPasswordHidden.value = false
                                originPasswordHidden.value = false
                                openPasswordDialog.value = false
                            },
                        ) {
                            Text(
                                "确认",
                                fontWeight = FontWeight.W700,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                newPasswordHidden.value = false
                                originPasswordHidden.value = false
                                openPasswordDialog.value = false
                            }
                        ) {
                            Text(
                                "取消",
                                fontWeight = FontWeight.W700,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 退出登录
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.error
                ),
                onClick = onLogOut
            ) {
                Text(text = "退出登录", fontSize = 18.sp)
            }
        }
    }
    }
}

@Composable
fun ImageChange(avatar: Uri, onNewImage: ()->Unit) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Box() {
            AsyncImage(
                model = avatar,
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    // Clip image to shaped as a circle
                    .size(200.dp)
                    .clip(CircleShape)
                    .clickable {/* TODO */ },
                alignment = Alignment.Center
            )

            IconButton(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(50.dp)
                    .align(Alignment.BottomEnd)
                    .background(color = MaterialTheme.colorScheme.secondaryContainer),
                onClick = { onNewImage() }) {
                Icon(
                    Icons.Default.PhotoCamera,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleInfoChange(onOpenBlockList : () -> Unit, myinfo: InfoData, infoEditViewModel: InfoEditViewModel, context: Context) {
    Card() {
        val openNameDialog = remember { mutableStateOf(false) }
        val openIntroDialog = remember { mutableStateOf(false) }
        val newNameText = remember { mutableStateOf("") }
        val newIntroText = remember { mutableStateOf("") }

        Row (modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 15.dp)
            .fillMaxWidth()) {
            Text(text = "用户名", modifier = Modifier.width(130.dp), fontSize = 20.sp, color = Color.Gray)
            Text(
                text = myinfo.ID,
                fontSize = 20.sp,
                modifier = Modifier.clickable{
                    openNameDialog.value = ! openNameDialog.value
                }
            )
        }

        if (openNameDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    // 当用户点击对话框以外的地方或者按下系统返回键将会执行的代码
                    openNameDialog.value = false
                },
                title = {
                    Text(
                        text = "修改用户名",
                        fontWeight = FontWeight.W700,
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                text = {
                    TextField(
                        value = newNameText.value,
                        onValueChange = {
                            newNameText.value = it
                        },
                        singleLine = true
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            openNameDialog.value = false
                            infoEditViewModel.modifyUserName(context, newNameText.value)
                        },
                    ) {
                        Text(
                            "确认",
                            fontWeight = FontWeight.W700,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            openNameDialog.value = false
                        }
                    ) {
                        Text(
                            "取消",
                            fontWeight = FontWeight.W700,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            )
        }

        Row (modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 5.dp)
            .fillMaxWidth()) {
            Text(text = "个人简介", modifier = Modifier.width(130.dp), fontSize = 20.sp, color = Color.Gray)
            Text(
                text = myinfo.SelfIntro,
                fontSize = 18.sp,
                modifier = Modifier.clickable{
                    openIntroDialog.value = !openIntroDialog.value
                }.fillMaxWidth(1f)
            )
        }

        if (openIntroDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    // 当用户点击对话框以外的地方或者按下系统返回键将会执行的代码
                    openIntroDialog.value = false
                },
                title = {
                    Text(
                        text = "修改个人简介",
                        fontWeight = FontWeight.W700,
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                text = {
                    TextField(
                        value = newIntroText.value,
                        onValueChange = {
                            newIntroText.value = it
                        },
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            openIntroDialog.value = false
                            infoEditViewModel.modifyDescription(context, newIntroText.value)
                        },
                    ) {
                        Text(
                            "确认",
                            fontWeight = FontWeight.W700,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            openIntroDialog.value = false
                        }
                    ) {
                        Text(
                            "取消",
                            fontWeight = FontWeight.W700,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            )
        }


        Text(
            text = "黑名单",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 15.dp)
                .clickable { onOpenBlockList() },
            fontSize = 20.sp,
            color = Color.Gray
        )
    }
}


@Preview
@Composable
fun InfoEditPreview() {
    InfoEditPage({}, {}, {}, infoEditViewModel = InfoEditViewModel())
}