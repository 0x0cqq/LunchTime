package com.thss.lunchtime.chat

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.thss.lunchtime.data.userPreferencesStore
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatPage(oppoSiteUserName: String, onBack: () -> Unit, chatPageViewModel: ChatPageViewModel) {
    val uiState = chatPageViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val userData = context.userPreferencesStore
    LaunchedEffect(Unit) {
        chatPageViewModel.connect(userData.data.first().userName, oppoSiteUserName)
        chatPageViewModel.getOppositeUserInfo(context, oppoSiteUserName)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = uiState.value.oppositeUserName)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
                        value = uiState.value.inputValue,
                        onValueChange = { chatPageViewModel.updateInputValue(it) },
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
                                    onClick = {
                                        chatPageViewModel.send(uiState.value.inputValue)
                                    }
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
        // 这里 data 是按照时间逆序排序，前面的是新的
        // 开了 reverse layout 之后，下面的是新的
        LazyColumn(
            modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues),
            reverseLayout = true
        ) {
            itemsIndexed(uiState.value.messageList) { index, message ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // index + 1 的是上面的那条
                    if (index + 1 < uiState.value.messageList.size) {
                        val nextMessage = uiState.value.messageList[index + 1]
                        if(SimpleDateFormat("MM-dd", Locale.CHINESE).format(message.time)
                            != SimpleDateFormat("MM-dd", Locale.CHINESE).format(nextMessage.time)) {
                            Text(SimpleDateFormat("MM-dd", Locale.CHINESE).format(message.time))
                        }
                    }
                    if(index + 1 == uiState.value.messageList.size) {
                        Text(SimpleDateFormat("MM-dd", Locale.CHINESE).format(message.time))
                    }
                    if (message.userName == uiState.value.oppositeUserName) {
                        // Opposite chat bubble
                        ChatBubbleOpposite(message)
                    } else {
                        ChatBubbleMine(message)
                    }

                }
            }
        }
    }
}


@Composable
fun ChatMessageAvatar(uri: Uri) {
    AsyncImage(
        uri,
        contentDescription = "heading",
        modifier = Modifier
            // Set image size to 40dp
            .size(40.dp)
            // Clip image to shaped as a circle
            .clip(CircleShape)
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ChatMessageBody(message: ChatData) {
    Card() {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)) {
            Row() {
                Text(
                    text = message.message,
                    fontSize = 16.sp
                )
            }
            Row(
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = SimpleDateFormat("HH:mm:ss", Locale.CHINESE).format(message.time),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun ChatBubbleMine(message: ChatData) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.padding(10.dp).fillMaxWidth()
    ) {
        ChatMessageBody(message)
        Spacer(modifier = Modifier.width(8.dp))
        ChatMessageAvatar(message.userAvatar)
    }
}


@Composable
fun ChatBubbleOpposite(message: ChatData) {
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.padding(10.dp).fillMaxWidth()
    ) {
        ChatMessageAvatar(message.userAvatar)
        Spacer(modifier = Modifier.width(8.dp))
        ChatMessageBody(message)
    }
}



@Preview
@Composable
fun ChatPreview() {
    ChatPage("123456", {}, ChatPageViewModel())
}

@Preview
@Composable
fun ChatBubbleMinePreview() {
    ChatBubbleMine(ChatData())
}

@Preview
@Composable
fun ChatBubbleOppositePreview() {
    ChatBubbleOpposite(ChatData())
}