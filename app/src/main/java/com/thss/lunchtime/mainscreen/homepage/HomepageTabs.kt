package com.thss.lunchtime.mainscreen.homepage

sealed class HomepageTabs(val name : String) {
    object byTime : HomepageTabs("时间")
    object byLike : HomepageTabs("点赞")
    object byComment : HomepageTabs("评论")
}
