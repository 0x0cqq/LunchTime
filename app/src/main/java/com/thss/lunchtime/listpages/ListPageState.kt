package com.thss.lunchtime.listpages

import com.thss.lunchtime.component.InfoData

data class ListPageState (
    val type : Int = -1,
    val userList : List<InfoData> = listOf()
)