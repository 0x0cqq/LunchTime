package com.thss.lunchtime.newpost

import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.TextRange
import me.onebone.parvenu.ParvenuEditorValue
import me.onebone.parvenu.ParvenuString

data class NewPostData(
    val title: String = "",
    val content: String = "",
    val location: String = "",
    val isLocationUsed : Boolean = false,
    val tag: String = "",
    val avatarUri: Uri = Uri.EMPTY,
    val isTagUsed : Boolean = false,
    val selectedImgUris: List<ImageBitmap> = listOf(),
    val richContent: ParvenuEditorValue = ParvenuEditorValue(
            parvenuString = ParvenuString(
                text = "",
                spanStyles = listOf(),
                paragraphStyles = listOf()
            ),
    selection = TextRange.Zero,
    composition = null
    )
)
