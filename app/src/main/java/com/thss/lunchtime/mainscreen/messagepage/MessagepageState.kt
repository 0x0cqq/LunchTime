package com.thss.lunchtime.mainscreen.messagepage

import com.thss.lunchtime.R
import com.thss.lunchtime.noticeData

data class MessagepageState (
    val selectedIndex : Int = 0,
    val NoticeDataLists : MutableList<List<noticeData>> = mutableListOf(listOf(), listOf(), listOf()),
)