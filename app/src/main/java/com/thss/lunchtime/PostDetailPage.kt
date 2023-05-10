package com.thss.lunchtime

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun PostDetailedPage(msg: PostData, type: PostType)
{
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(text = "动态详情")
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Share,
                            contentDescription = null)
                    }
                },
            )
        }
    ) {
        paddingValues -> Column(modifier = Modifier
        .fillMaxWidth()
        .padding(paddingValues)
        .verticalScroll(
            rememberScrollState()
        )) {
            PostMainBody(msg = msg, type = type)
        }
    }
}

@Preview
@Composable
fun PostDetailedCardPreview() {
    PostDetailedPage(
        msg = PostData(
            Type = 3,
            graphResources = arrayOf(R.drawable.wp, R.drawable.wp, R.drawable.wp)
        ),
        type = PostType(Detailed = true)
    )
}