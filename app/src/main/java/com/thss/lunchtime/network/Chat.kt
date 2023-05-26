package com.thss.lunchtime.network

import android.net.Uri
import androidx.core.net.toUri
import com.thss.lunchtime.chat.ChatData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class ChatMessage(
    @SerialName("user_name")
    val userName: String,
    @SerialName("user_avatar")
    val avatar: String? = null,
    val content: String,
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
        userName = this.userName,
        userAvatar = if(this.avatar == null) Uri.EMPTY else this.avatar.toUri() ,
        message = this.content,
        time = Date(this.timestamp!! * 1000),
    )
}