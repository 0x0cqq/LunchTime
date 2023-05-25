package com.thss.lunchtime.network

import com.thss.lunchtime.chat.ChatData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class ChatMessage(
    @SerialName("sender_id")
    val senderID: Int,
    val message: String,
    val timestamp: Long? = null,
)

@Serializable
data class ChatRequest(
    val type: String,
    val message: ChatMessage? = null,
)

@Serializable
data class ChatResponse(
    val type : String,
    val message: ChatMessage? = null,
    @SerialName("history")
    val messageList : List<ChatMessage>? = null,
)

fun ChatMessage.toChatData(): ChatData {
    return ChatData(
        userID = this.senderID,
        message = this.message,
        time = Date(this.timestamp!! * 1000),
    )
}