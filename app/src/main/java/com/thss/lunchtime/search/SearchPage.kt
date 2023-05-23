package com.thss.lunchtime.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

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

    }
    }
}

@Preview
@Composable
fun SearchPagePreview(){
    SearchPage()
}