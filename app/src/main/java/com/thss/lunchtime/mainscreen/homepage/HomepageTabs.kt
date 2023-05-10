package com.thss.lunchtime.mainscreen.homepage

sealed class HomepageTabs(val name : String) {
    object byTime : HomepageTabs("By time")
    object byLike : HomepageTabs("By like")
    object byFav : HomepageTabs("By favourite")
}
