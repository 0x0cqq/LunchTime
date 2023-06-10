package com.thss.lunchtime.search

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thss.lunchtime.post.PostReviewCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPage(onClickPostPreviewCard: (postID : Int) -> Unit, onOpenUserInfo: (userName: String) -> Unit, searchPageViewModel: SearchPageViewModel) {

    val alreadySearched = remember { mutableStateOf(false) }
    val uiState = searchPageViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = { SearchPageTopBar(
            onClickSearch = { field: Int, keyword: String ->
                if(keyword.isNotEmpty()){
                    alreadySearched.value = true
                    searchPageViewModel.onClickSearch(context, field, keyword)
                } else {
                    Toast.makeText(context, "搜索关键词不能为空", Toast.LENGTH_SHORT).show()
                }
            })
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .clickable(onClick = { focusManager.clearFocus() },
                indication = null,
                interactionSource = remember {
                    MutableInteractionSource()
                })) {
            items(uiState.value.postDataList) { postData ->
                PostReviewCard(
                    onClickLike = { searchPageViewModel.onClickLike(context, postData.postID) },
                    onClickStar = { searchPageViewModel.onClickStar(context, postData.postID) },
                    onClickTopBar = { onOpenUserInfo(postData.publisherID) },
                    modifier = Modifier.clickable {
                        onClickPostPreviewCard(postData.postID)
                    },
                    msg = postData
                )
            }
            item {
                if(alreadySearched.value && uiState.value.postDataList.isEmpty()){
                    Text(text = "No result", modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp))
                }
            }
        }
    }
}

@Preview
@Composable
fun SearchPagePreview(){
    SearchPage({}, {}, SearchPageViewModel())
}