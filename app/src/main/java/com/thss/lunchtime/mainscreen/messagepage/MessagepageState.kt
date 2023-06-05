package com.thss.lunchtime.mainscreen.messagepage

import com.thss.lunchtime.component.NoticeData

data class MessagepageState (
    // 0: comment, 1: like, 2: chat
    val selectedIndex : Int = 0,
    val isRefreshing : Boolean = false,
    val noticeDataLists : MutableList<List<NoticeData>> = mutableListOf(listOf(), listOf(), listOf()),
)