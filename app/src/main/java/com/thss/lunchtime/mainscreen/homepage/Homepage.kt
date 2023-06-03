package com.thss.lunchtime.mainscreen.homepage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.thss.lunchtime.post.PostReviewCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun Homepage(onClickSearch: ()->Unit, onClickPostPreviewCard: (postID : Int) -> Unit, onClickNewPost: () -> Unit, onOpenUserInfo: (userName: String) -> Unit,homepageViewModel: HomepageViewModel) {
    val uiState = homepageViewModel.uiState.collectAsState()
    val tabs = listOf(HomepageTabs.byTime, HomepageTabs.byLike, HomepageTabs.byComment)
    val context = LocalContext.current

    val state = rememberPullRefreshState(refreshing = uiState.value.isRefreshing, onRefresh = {
        homepageViewModel.refresh(context)
    })

    // refresh on the launch
    LaunchedEffect(uiState.value.selectedIndex) {
        homepageViewModel.refresh(context)
    }

    Scaffold(
        topBar = {
            Column {
                HomePageTopBar(
                    onSwitchDropDownMenu = { index: Int ->
                        homepageViewModel.onSwitchDropDownMenu(context, index) },
                    onClickSearch = onClickSearch)
                if (uiState.value.dropMenuIndex != 1) {
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
            }
        },
        modifier = Modifier.padding(10.dp),
        floatingActionButton = {
            if (uiState.value.selectedIndex == tabs.indexOf(HomepageTabs.byTime)) {
                FloatingActionButton(
                    onClick = {
                        onClickNewPost()
//                              homepageViewModel.addRandomPost()
                    },
                ) {
                    Icon(Icons.Filled.Add, "Add")
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).pullRefresh(state = state)) {
            LazyColumn( modifier = Modifier.fillMaxWidth()) {
                items(uiState.value.postDataList) { postData ->
                    PostReviewCard(
                        onClickLike = {homepageViewModel.onClickLike(context, postData.postID)},
                        onClickStar = {homepageViewModel.onClickStar(context, postData.postID)},
                        onClickTopBar = {onOpenUserInfo(postData.publisherID)},
                        modifier = Modifier.clickable { 
                            onClickPostPreviewCard(postData.postID)
                        },
                        msg = postData)
                }
            }
            PullRefreshIndicator(uiState.value.isRefreshing, state, Modifier.align(Alignment.TopCenter))
        }
    }
}


@Composable
fun HomePageTopBar(onSwitchDropDownMenu: (index: Int) -> Unit, onClickSearch: () -> Unit) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedMenuIndex by rememberSaveable { mutableStateOf(0) }
    val dropDownMenuItems = listOf("综合", "热度", "我的关注", "Divider", "Tag1", "Tag2")
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize(Alignment.TopStart),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    dropDownMenuItems[selectedMenuIndex],
                    modifier = Modifier.padding(start = 15.dp)
                )
                IconButton(onClick = { expanded = true }) {
                    if(expanded) {
                        Icon(Icons.Default.ArrowLeft, contentDescription = "Shrink")
                    } else {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Expand More")
                    }
                }
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                dropDownMenuItems.forEachIndexed { index, name ->
                    if (name == "Divider") {
                        Divider()
                    } else {
                        DropdownMenuItem(
                            text = { Text(name) },
                            onClick = {
                                selectedMenuIndex = index
                                expanded = false
                                onSwitchDropDownMenu(selectedMenuIndex) },
                            modifier = Modifier.wrapContentHeight()
                        )
                    }
                }
            }
        }
        IconButton(onClick = onClickSearch) {
            Icon(Icons.Outlined.Search, contentDescription = "Search")
        }
    }
}