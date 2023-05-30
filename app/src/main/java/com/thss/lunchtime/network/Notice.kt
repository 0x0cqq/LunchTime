package com.thss.lunchtime.network

import androidx.core.net.toUri
import com.thss.lunchtime.NoticeData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Notice(
    @SerialName("user_name")
    val userName : String = "",
    @SerialName("user_image")
    val userImage : String = "",
    @SerialName("create_time")
    val createTime: Long,
    @SerialName("comment")
    val content: String = "",
    @SerialName("picture")
    val pictureURL: String = "",
    @SerialName("post_id")
    val postId: Int = -1,
)

fun Notice.toNoticeData(selectedIndex : Int) : NoticeData {
    return NoticeData(
        noticerAvatar = userImage.toUri(),
        noticerID = userName,
        noticeDate = Date(createTime * 1000),
        noticeType = selectedIndex + 1,
        reply = content,
        postId = postId,
        refData = pictureURL
    )
}