package com.thss.lunchtime.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    @SerialName("sender_id")
    val senderID: Int,
    val message: String,
    val timestamp: Long
)

@Serializable
data class ChatList(
    @SerialName("chat_list")
    val chatList: List<ChatMessage>
)