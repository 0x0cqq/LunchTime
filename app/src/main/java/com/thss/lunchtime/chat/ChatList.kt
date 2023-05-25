package com.thss.lunchtime.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.thss.lunchtime.noticeData
import com.thss.lunchtime.noticePreviewCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatList() {
    Card(Modifier.fillMaxWidth()) {
        LazyColumn(modifier = Modifier.fillMaxSize()
        ) {
            items(chatArray) {chatItem ->
                noticePreviewCard(msg = chatItem, onClickNotice = {})
            }
        }
    }
}


val chatArray = listOf(
    noticeData(noticeType = 3, reply = "你说得对1"),
    noticeData(noticeType = 3, reply = "你说得不对"),
    noticeData(noticeType = 3, reply = "ddl真的要做不完了"),
    noticeData(noticeType = 3, reply = "能不能不做了")
)

@Preview
@Composable
fun chatListPreview() {
    ChatList()
}