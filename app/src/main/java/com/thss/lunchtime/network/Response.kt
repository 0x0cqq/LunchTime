package com.thss.lunchtime.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Response(
    val status: Boolean,
    val message: String
)

@Serializable
data class ResponseWithNotice(
    val status: Boolean = false,
    val message: String = "",
    @SerialName("notice_list")
    val noticeList: List<Notice> = listOf()
)
