package com.thss.lunchtime.search

import com.thss.lunchtime.post.PostData

data class SearchPageState (
    val field: Int = -1,
    val postDataList: List<PostData> = listOf()
)