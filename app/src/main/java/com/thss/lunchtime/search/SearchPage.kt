package com.thss.lunchtime.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.thss.lunchtime.mainscreen.infopage.postArray
import com.thss.lunchtime.post.PostReviewCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPage()
{
    Scaffold(
        topBar = { SearchPageTopBar() }
    ) {
        paddingValues ->  Column(modifier = Modifier
        .fillMaxWidth()
        .padding(paddingValues)) {
        LazyColumn(modifier = Modifier.fillMaxSize()
        ) {
            items(postArray) { postData ->
                PostReviewCard({}, {}, msg = postData)
            }
        }
    }
    }
}

@Preview
@Composable
fun SearchPagePreview(){
    SearchPage()
}