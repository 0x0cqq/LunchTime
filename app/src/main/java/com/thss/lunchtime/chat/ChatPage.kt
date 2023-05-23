package com.thss.lunchtime.chat

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thss.lunchtime.LikeBtn
import com.thss.lunchtime.R
import com.thss.lunchtime.StarBtn
import com.thss.lunchtime.component.CommentComp
import com.thss.lunchtime.component.PostMainBody
import com.thss.lunchtime.component.PostType
import com.thss.lunchtime.post.PostDetailViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun ChatPage()
{
    var inputText = remember {
        mutableStateOf("")
    }
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(text = "用户名XXXX")
                },
                navigationIcon = {
                    IconButton(onClick = {  }) {
                        Icon(imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "back")
                    }
                },
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
            ) {

                Spacer(modifier = Modifier.width(8.dp))

                // 自定义TextField
                Box(modifier = Modifier.fillMaxWidth(1f)) {
                    BasicTextField(
                        value = inputText.value,
                        onValueChange = { inputText.value = it },
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(25)
                            )
                            .height(40.dp)
                            .fillMaxWidth(),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                        decorationBox = { innerTextField ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    innerTextField()
                                }
                                IconButton(
                                    onClick = {  }
                                ) {
                                    Icon(
                                        Icons.Filled.Send,
                                        "Send",
                                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            }
                        }
                    )
                }
            }
        },
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
        ) {

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBubble_u() {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.padding(end = 70.dp, start = 10.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.touxaingnvhai),
            contentDescription = "heading",
            modifier = Modifier
                // Set image size to 40dp
                .size(40.dp)
                // Clip image to shaped as a circle
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Card() {
            Column(modifier = Modifier.padding(horizontal = 15.dp)) {
                Row() {
                    Text(
                        text = "这是聊天内容\n这是聊天内容\n这是聊天内容",
                        fontSize = 16.sp
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = SimpleDateFormat("HH:mm", Locale.CHINESE).format(Date()),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBubble_i() {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.padding(start = 70.dp, end = 10.dp)
    ) {
        Card() {
            Column(modifier = Modifier.padding(horizontal = 15.dp)) {
                Row() {
                    Text(
                        text = "这是聊天内容\n这是聊天内容\n这是聊天内容",
                        fontSize = 16.sp
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = SimpleDateFormat("HH:mm", Locale.CHINESE).format(Date()),
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Image(
            painter = painterResource(id = R.drawable.touxaingnvhai),
            contentDescription = "heading",
            modifier = Modifier
                // Set image size to 40dp
                .size(40.dp)
                // Clip image to shaped as a circle
                .clip(CircleShape)
        )
    }
}



@Preview
@Composable
fun ChatPreview() {
    ChatPage()
}

@Preview
@Composable
fun ChatBubblePreview() {
    ChatBubble_u()
}

@Preview
@Composable
fun ChatBubbleiPreview() {
    ChatBubble_i()
}