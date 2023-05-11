package com.thss.lunchtime.newpost

import androidx.compose.ui.graphics.ImageBitmap

data class NewPostData(
    val title: String = "",
    val content: String = "",
    val location: String = "",
    val isLocationUsed : Boolean = false,
    val tag: String = "",
    val isTagUsed : Boolean = false,
    val selectedImgUris: List<ImageBitmap> = listOf()
)
