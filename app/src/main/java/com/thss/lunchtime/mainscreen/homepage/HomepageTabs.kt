package com.thss.lunchtime.mainscreen.homepage

sealed class HomepageTabs(val name : String) {
    object byTime : HomepageTabs("时间")
    object byPopularity : HomepageTabs("热度")
    object byComment : HomepageTabs("评论")
}
