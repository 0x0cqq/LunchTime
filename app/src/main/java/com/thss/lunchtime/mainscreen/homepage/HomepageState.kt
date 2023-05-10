package com.thss.lunchtime.mainscreen.homepage

import com.thss.lunchtime.PostData

data class HomepageState(
    val selectedIndex : Int = 0,
    val postDataList : List<PostData> = listOf()
)
