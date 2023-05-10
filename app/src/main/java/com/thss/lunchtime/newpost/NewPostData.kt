package com.thss.lunchtime.newpost

import android.net.Uri

data class NewPostData(
    val title: String = "",
    val content: String = "",
    val location: String = "",
    val imgUris: List<Uri> = listOf()
)
