package com.thss.lunchtime.mainscreen.messagepage


sealed class MessageTabs(val name : String) {
    object Comment : MessageTabs("评论")
    object Like : MessageTabs("点赞")
    object Chat : MessageTabs("聊天")
}