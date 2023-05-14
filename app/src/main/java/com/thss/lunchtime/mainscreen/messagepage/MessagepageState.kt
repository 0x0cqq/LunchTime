package com.thss.lunchtime.mainscreen.messagepage

import com.thss.lunchtime.R
import com.thss.lunchtime.noticeData
import com.thss.lunchtime.refData

data class MessagepageState (
    val selectedIndex : Int = 0,
    val commentDataList : List<noticeData> = listOf(
        noticeData(noticeType = 1, reply = "你说得对"),
        noticeData(noticeType = 1, reply = "你说得不对")
    ),
    val likeDataList: List<noticeData> = listOf(
        noticeData(
        noticeType = 2,
        refData = refData(graphResources = arrayOf(R.drawable.wp, R.drawable.wp, R.drawable.wp)),
    ), noticeData(
        noticeType = 2,
        refData = refData(graphResources = arrayOf(R.drawable.wp, R.drawable.wp, R.drawable.wp)),
    )),
    val chatDataList: List<noticeData> = listOf(
        noticeData(noticeType = 3, reply = "在嘛"),
        noticeData(noticeType = 3, reply = "晚上出来约个饭呗")
    )
)