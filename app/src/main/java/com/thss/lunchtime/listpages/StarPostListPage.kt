package com.thss.lunchtime.listpages

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thss.lunchtime.post.PostReviewCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun StarPostListPage(onBack: ()->Unit, onOpenUserInfo: (userName: String) -> Unit, onOpenPost:(postId: Int)->Unit, userName: String, starPostListViewModel: StarPostListViewModel = viewModel()) {
    val context = LocalContext.current
    val uiState = starPostListViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        starPostListViewModel.refresh(context, userName)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack ) {
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
                items(uiState.value.postDataList) { postData ->
                    PostReviewCard(
                        onClickLike = { starPostListViewModel.onClickLike(context, postData.postID) },
                        onClickStar = { starPostListViewModel.onClickStar(context, postData.postID) },
                        onClickTopBar = { onOpenUserInfo(postData.publisherID) },
                        msg = postData,
                        modifier = Modifier.clickable { onOpenPost(postData.postID) })
                }
            }
        }
    }
    }
}

@Preview
@Composable
fun StarListPreview() {
    StarPostListPage(onBack = {}, onOpenUserInfo = {}, userName = "", onOpenPost = {},)
}