package com.thss.lunchtime.chat

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatPage(onBack: () -> Unit, chatPageViewModel: ChatPageViewModel) {
    val uiState = chatPageViewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(text = uiState.value.userName)
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
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
        ) {
            items(uiState.value.messageList) { message ->
                if (message.userID == uiState.value.userID) {
                    // Opposite chat bubble
                    ChatBubbleOpposite(message)
                } else {
                    ChatBubbleMine(message)
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
fun ChatMessageBody(message: ChatMessage) {
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
                    text = SimpleDateFormat("HH:mm", Locale.CHINESE).format(message.time),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun ChatBubbleMine(message: ChatMessage) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.padding(start = 70.dp, end = 10.dp)
    ) {
        ChatMessageBody(message)
        Spacer(modifier = Modifier.width(8.dp))
        ChatMessageAvatar(message.userAvatar)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBubbleOpposite(message: ChatMessage) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.padding(end = 70.dp, start = 10.dp)
    ) {
        ChatMessageAvatar(message.userAvatar)
        Spacer(modifier = Modifier.width(8.dp))
        ChatMessageBody(message)
    }
}



@Preview
@Composable
fun ChatPreview() {
    ChatPage({}, ChatPageViewModel())
}

@Preview
@Composable
fun ChatBubbleMinePreview() {
    ChatBubbleMine(ChatMessage())
}

@Preview
@Composable
fun ChatBubbleOppositePreview() {
    ChatBubbleOpposite(ChatMessage())
}