package com.thss.lunchtime.mainscreen.infopage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.thss.lunchtime.R
import com.thss.lunchtime.component.InfoComp
import com.thss.lunchtime.component.InfoData
import com.thss.lunchtime.post.PostData
import com.thss.lunchtime.post.PostReviewCard

class InfoEditViewModel(savedStateHandle: SavedStateHandle): ViewModel() {

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun InfoEditPage(myinfo: InfoData) {
    Scaffold(
        topBar = {
            SmallTopAppBar(
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
            SimpleInfoChange(myinfo)

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
                onClick = { /*TODO*/ }
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
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleInfoChange(myinfo: InfoData) {
    Card() {
        Row (modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 15.dp)
            .fillMaxWidth()) {
            Text(text = "Username", modifier = Modifier.width(130.dp), fontSize = 20.sp, color = Color.Gray)
            Text(
                text = myinfo.ID,
                fontSize = 20.sp,
                modifier = Modifier.clickable{}
            )
        }

        Column (modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 5.dp)
            .fillMaxWidth()) {
            Text(text = "个人简介", modifier = Modifier.width(130.dp), fontSize = 20.sp, color = Color.Gray)
            Text(
                text = myinfo.SelfIntro,
                fontSize = 16.sp,
                modifier = Modifier.clickable{}
            )
        }

        Text(
            text = "黑名单",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 15.dp),
            fontSize = 20.sp,
            color = Color.Gray
        )
    }
}


@Preview
@Composable
fun InfoEditPreview() {
    InfoEditPage(myinfo = InfoData())
}