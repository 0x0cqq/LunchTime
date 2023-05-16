package com.thss.lunchtime.post

import com.thss.lunchtime.component.CommentData

data class PostDetailData (
    val postData: PostData = PostData(),
    val commentDataList: List<CommentData> = listOf(),
    val currentCommentInput : String = "",
    val isRefreshing: Boolean = false,
)