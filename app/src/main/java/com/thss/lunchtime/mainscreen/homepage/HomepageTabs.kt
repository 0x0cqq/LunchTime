package com.thss.lunchtime.mainscreen.homepage

sealed class HomepageTabs(val name : String) {
    object byTime : HomepageTabs("By time")
    object byPopularity : HomepageTabs("By Popularity")
    object byComment : HomepageTabs("By comment")
}
