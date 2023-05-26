package com.thss.lunchtime.chat

data class ChatPageState(
    val oppositeUserName : String = "test_user_name",
    val messageList : List<ChatData> = listOf(),
    val inputValue : String = ""
)
