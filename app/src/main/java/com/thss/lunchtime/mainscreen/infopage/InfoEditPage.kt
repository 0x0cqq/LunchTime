package com.thss.lunchtime.mainscreen.infopage

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thss.lunchtime.R
import com.thss.lunchtime.component.InfoData


// 除了注销，其他（修改密码blabla）的应该都可以在这里再开一个 navigation 解决？
// 就不要往上走了 上面好拥挤
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun InfoEditPage(onBack: () -> Unit, onLogOut: () -> Unit, infoEditViewModel: InfoEditViewModel) {
    val infoData = infoEditViewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        infoEditViewModel.refresh(context)
    }
    Scaffold(
        topBar = {
            SmallTopAppBar(
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
            .fillMaxWidth()
            .padding(paddingValues)
    ) {
        Column (
            modifier = Modifier
            .fillMaxWidth()
        ) {
            ImageChange()
            SimpleInfoChange(infoData.value)

            Spacer(modifier = Modifier.height(50.dp))

            // 修改密码
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                onClick = { /*TODO*/ }
            ) {
                Text(text = "修改密码", fontSize = 18.sp)
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
fun ImageChange() {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.touxaingnvhai),
            contentDescription = "heading",
            modifier = Modifier
                // Set image size to 40dp
                .size(200.dp)
                // Clip image to shaped as a circle
                .clip(CircleShape)
                .clickable { /*TODO*/ }
        )

        IconButton(
            modifier = Modifier
                .offset((-57).dp, (-3).dp)
                .clip(CircleShape)
                .size(45.dp),
            colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White),
            onClick = { /*TODO*/ }) {
            Icon(
                Icons.Default.PhotoCamera,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleInfoChange(myinfo: InfoData) {
    Card() {
        var openNameDialog = remember { mutableStateOf(true) }
        var openIntroDialog = remember { mutableStateOf(false) }
        var newNameText = remember { mutableStateOf(myinfo.ID) }
        var newIntroText = remember { mutableStateOf(myinfo.SelfIntro) }

        Row (modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 15.dp)
            .fillMaxWidth()) {
            Text(text = "Username", modifier = Modifier.width(130.dp), fontSize = 20.sp, color = Color.Gray)
            Text(
                text = myinfo.ID,
                fontSize = 20.sp,
                modifier = Modifier.clickable{
                    openNameDialog.value = ! openNameDialog.value
                }
            )
        }

        if ( openNameDialog.value) {
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

        Column (modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 5.dp)
            .fillMaxWidth()) {
            Text(text = "个人简介", modifier = Modifier.width(130.dp), fontSize = 20.sp, color = Color.Gray)
            Text(
                text = myinfo.SelfIntro,
                fontSize = 16.sp,
                modifier = Modifier.clickable{
                    openIntroDialog.value = !openIntroDialog.value
                }
            )
        }

        if ( openIntroDialog.value) {
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
                .clickable {  },
            fontSize = 20.sp,
            color = Color.Gray
        )
    }
}


@Preview
@Composable
fun InfoEditPreview() {
    InfoEditPage({}, {}, infoEditViewModel = InfoEditViewModel())
}