package com.thss.lunchtime.listpages

import com.thss.lunchtime.post.PostData

data class StarPostListState(
    val postDataList : List<PostData> = listOf()
)