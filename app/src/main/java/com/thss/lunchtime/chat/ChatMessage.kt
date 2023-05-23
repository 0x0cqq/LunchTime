package com.thss.lunchtime.chat

import android.net.Uri
import java.util.Date

data class ChatMessage (
    val userID : Int = 0,
    val userAvatar: Uri = Uri.EMPTY,
    val userName : String = "test_user_name",
    val message : String = "message",
    val time : Date = Date()
)
