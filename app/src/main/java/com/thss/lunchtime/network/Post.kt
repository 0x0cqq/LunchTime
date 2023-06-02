package com.thss.lunchtime.network

import com.thss.lunchtime.post.PostData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.core.net.toUri
import com.thss.lunchtime.component.CommentData
import java.util.Date

@Serializable
data class Comment (
    @SerialName("user_name")
    val userName: String = "",
    @SerialName("user_image")
    val userAvatar: String = "",
    val content: String = "",
    @SerialName("create_time")
    val createTime: Long = 0,
)

fun Comment.toCommentData() : CommentData {
    return CommentData(
        commentID = userName,
        commentAvatar = userAvatar.toUri(),
        commentDate = Date(createTime * 1000),
        commentContent = content
    )
}

@Serializable
data class Post (
    @SerialName("post_id")
    val postID: Int = 0,
    @SerialName("user_name")
    val name: String = "",
    @SerialName("user_image")
    val avatar: String = "",
    val tag: String = "",
    val location: String = "",
    val title: String = "",
    val content: String = "",
    @SerialName("create_time")
    val createTime: Long,
    // TODO: 后端统一名称
    @SerialName("love_count")
    val likeCount: Int = 0,
    @SerialName("is_loved")
    val isLiked: Boolean = false,
    // TODO: 后端统一名称
    @SerialName("save_count")
    val starCount: Int = 0,
    @SerialName("is_saved")
    val isStared: Boolean = false,
    @SerialName("comment_count")
    val commentCount: Int = 0,
    @SerialName("picture")
    val pictureURLs : List<String> = listOf(),
    @SerialName("video")
    val videoURLs : List<String> = listOf(),
    @SerialName("is_video")
    val isVideo: Boolean = false
)

fun Post.toPostData() : PostData {
    return PostData(
        postID = postID,
        publisherID = name,
        publisherAvatar = avatar.toUri(),
        tag = tag,
        location = location,
        title = title,
        content = content,
        publishDate = Date(createTime * 1000),
        likeCount = likeCount,
        isLiked = isLiked,
        starCount = starCount,
        isStared = isStared,
        commentCount = commentCount,
        graphResources = pictureURLs.map{ it.toUri() }
    )
}