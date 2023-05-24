package com.thss.lunchtime.listpages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.thss.lunchtime.component.InfoData
import com.thss.lunchtime.component.InfoPreviewComp
import com.thss.lunchtime.mainscreen.infopage.postArray
import com.thss.lunchtime.post.PostData
import com.thss.lunchtime.post.PostReviewCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun StarListPage(postList: List<PostData>) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {

    }
    Scaffold(
        topBar = {
            SmallTopAppBar(
                navigationIcon = {
                    IconButton(onClick = {  }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                },
                title = {
                    Text(text = "收藏列表")
                }
            )
        }
    ) { paddingValues -> Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()
            ) {
                items(postList) { postData ->
                    PostReviewCard({}, {}, {}, msg = postData)
                }
            }
        }
    }
    }
}

@Preview
@Composable
fun StarListPreview() {
    StarListPage(postList = postArray)
}