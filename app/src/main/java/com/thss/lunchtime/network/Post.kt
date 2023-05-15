package com.thss.lunchtime.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@Serializable
data class Comment (
    @SerialName("user_name")
    val userName: String = "",
    val content: String = "",
    @SerialName("create_time")
    val createTime: String = "",
)

@Serializable
data class Post (
    @SerialName("post_id")
    val postID: Int = 0,
    @SerialName("user_name")
    val userName: String = "",
    val tag: String = "",
    val location: String = "",
    val title: String = "",
    val content: String = "",
    @SerialName("create_time")
    val createTime: String,
    @SerialName("love_count")
    val loveCount: Int = 0,
    @SerialName("comment_count")
    val commentCount: Int = 0,
    @SerialName("save_count")
    val saveCount: Int = 0,
    @SerialName("picture")
    val pictureURLs : List<String> = listOf(),

)