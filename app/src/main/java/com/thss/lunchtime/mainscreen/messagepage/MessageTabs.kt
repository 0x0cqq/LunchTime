package com.thss.lunchtime.mainscreen.messagepage


sealed class MessageTabs(val name : String) {
    object Comment : MessageTabs("Comment")
    object Like : MessageTabs("Like")
    object Chat : MessageTabs("Chat")
}