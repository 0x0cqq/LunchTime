package com.thss.lunchtime

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostReviewCard(msg: PostData)
{
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp)
    ) {
        PostMainBody(msg = msg)
        LikeStarComment(msg = msg)
    }
}

@Preview
@Composable
fun PostPreviewCardPreview() {
    PostReviewCard(
        msg = PostData(
            Type = 3,
            graphResources = arrayOf(R.drawable.wp, R.drawable.wp, R.drawable.wp)
        )
    )
}