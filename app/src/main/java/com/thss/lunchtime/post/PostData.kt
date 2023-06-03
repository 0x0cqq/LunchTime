package com.thss.lunchtime.post

import android.net.Uri
import java.util.Date

data class PostData(
    val publisherAvatar: Uri = Uri.EMPTY,
    val publisherID: String = "User_default",
    val publishDate: Date = Date(),
    val postID: Int = 0,
    val title: String = "title",
    val content: String = "content",
    val commentCount: Int = 0,
    val likeCount: Int = 0,
    val starCount: Int = 0,
    val isLiked: Boolean = false,
    val isStared: Boolean = false,
    val Type: Int = -1,
    val tag: String = "Tag",
    val location: String = "Location",
    val graphResources : List<Uri> = listOf(),
    val videoResources : List<Uri> = listOf(),
    val isVideo: Boolean = false,
    val publisherStatus: Int = 2,
)