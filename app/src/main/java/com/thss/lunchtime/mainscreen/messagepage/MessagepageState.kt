package com.thss.lunchtime.mainscreen.messagepage

import com.thss.lunchtime.R
import com.thss.lunchtime.noticeData
import com.thss.lunchtime.refData

data class MessagepageState (
    val selectedIndex : Int = 0,
    val NoticeDataLists : MutableList<List<noticeData>> = mutableListOf(
        listOf(
            noticeData(noticeType = 1, reply = "你说得对"),
            noticeData(noticeType = 1, reply = "你说得不对")
        ),
        listOf(
            noticeData(
            noticeType = 2,
            refData = refData(graphResources = arrayOf("http://82.156.30.206:8000/media/postImage/1684160879_0.jpeg"))
            ),
            noticeData(
            noticeType = 2,
            refData = refData(graphResources = arrayOf("http://82.156.30.206:8000/media/postImage/1684160879_0.jpeg")),
            )
        ),
        listOf(
            noticeData(noticeType = 3, reply = "在嘛"),
            noticeData(noticeType = 3, reply = "晚上出来约个饭呗")
        )
    ),
)