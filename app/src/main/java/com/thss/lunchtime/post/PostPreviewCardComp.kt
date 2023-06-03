package com.thss.lunchtime.post

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.thss.lunchtime.component.LikeStarComment
import com.thss.lunchtime.component.PostMainBody
import com.thss.lunchtime.component.PostType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostReviewCard(onClickLike: () -> Unit, onClickStar: () -> Unit, onClickTopBar: () -> Unit, msg: PostData, onClickVideo: (url: String) -> Unit = {}, modifier: Modifier = Modifier)
{
    Card(
//        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
        .fillMaxWidth()
        .padding(5.dp)
    ) {
        PostMainBody(msg = msg, type = PostType(false), onClickTopBar = onClickTopBar, onClickVideo = onClickVideo)
        LikeStarComment(onClickLike = onClickLike, onClickStar = onClickStar, msg = msg)
    }
}

@Preview
@Composable
fun PostPreviewCardPreview() {
    PostReviewCard(
        onClickLike = {},
        onClickStar = {},
        onClickTopBar = {},
        onClickVideo = {},
        msg = PostData(
            Type = 3,
            graphResources = listOf(
                "https://www.shanghai.gov.cn/assets2020/img/zjsh1.jpg".toUri(),
                "https://www.shanghai.gov.cn/assets2020/img/zjsh1.jpg".toUri(),
                "https://www.shanghai.gov.cn/assets2020/img/zjsh1.jpg".toUri(),
                "https://www.shanghai.gov.cn/assets2020/img/zjsh1.jpg".toUri(),
            )
        ),

    )
}