package com.thss.lunchtime.mainscreen.homepage

import com.thss.lunchtime.post.PostData

data class HomepageState(
    // homepage 上面的 tab，决定按什么顺序显示所有帖子
    val selectedIndex : Int = 0,
    val isRefreshing: Boolean = false,
    val postDataList : List<PostData> = listOf()
)
