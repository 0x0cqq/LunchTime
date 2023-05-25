package com.thss.lunchtime.chat

data class ChatPageState(
    val userID : Int = 0,
    val userName : String = "test_user_name",
    val messageList : List<ChatData> = listOf(),
    val inputValue : String = ""
)
