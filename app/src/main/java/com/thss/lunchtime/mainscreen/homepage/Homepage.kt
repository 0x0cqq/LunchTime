package com.thss.lunchtime.mainscreen.homepage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thss.lunchtime.PostData
import com.thss.lunchtime.PostPreviewCard
import com.thss.lunchtime.mainscreen.HomePageTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun Homepage(onClickNewPost: () -> Unit = {}, homepageViewModel: HomepageViewModel = viewModel()) {
    val uiState = homepageViewModel.uiState.collectAsState()
    val tabs = listOf(HomepageTabs.byTime, HomepageTabs.byLike, HomepageTabs.byFav)
    Scaffold(
        topBar = {
            Column {
                HomePageTopBar()
                ScrollableTabRow(
                    selectedTabIndex = uiState.value.selectedIndex,
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    tabs.forEachIndexed { index, homepageTabs ->
                        Tab(
                            selected = uiState.value.selectedIndex == index,
                            onClick = { homepageViewModel.selectTab(index) },
                            text = {
                                Text(homepageTabs.name)
                            }
                        )
                    }
                }
            }
        },
        modifier = Modifier.padding(10.dp),
        floatingActionButton = {
            if (uiState.value.selectedIndex == tabs.indexOf(HomepageTabs.byTime)) {
                FloatingActionButton(
                    onClick = {
//                        onClickNewPost()
                              homepageViewModel.addRandomPost()
                    },
                ) {
                    Icon(Icons.Filled.Add, "Add")
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(uiState.value.postDataList) { postData ->
                PostPreviewCard(postData)
            }
        }
    }
}
