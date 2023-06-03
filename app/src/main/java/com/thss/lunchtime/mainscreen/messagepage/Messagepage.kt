package com.thss.lunchtime.mainscreen.messagepage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thss.lunchtime.NoticeData
import com.thss.lunchtime.NoticePreviewCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun Messagepage(onClickChat: (oppositeUserName: String) -> Unit, onClickPostNotice: (postId: Int) -> Unit , messageViewModel: MessageViewModel = viewModel()) {
    val uiState = messageViewModel.uiState.collectAsState()
    val tabs = listOf(MessageTabs.Comment, MessageTabs.Like, MessageTabs.Chat)
    val context = LocalContext.current

    val state = rememberPullRefreshState(refreshing = uiState.value.isRefreshing, onRefresh = {
        messageViewModel.refresh(context)
    })

    LaunchedEffect(uiState.value.selectedIndex) {
       messageViewModel.refresh(context)
    }

    Scaffold(
        topBar = {
            Column {
                MessagePageTopBar()
                TabRow(
                    selectedTabIndex = uiState.value.selectedIndex,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    tabs.forEachIndexed { index, messageTabs ->
                        Tab(
                            modifier = Modifier.weight(1f),
                            selected = uiState.value.selectedIndex == index,
                            onClick = { messageViewModel.selectTab(index) },
                            text = {
                                Text(messageTabs.name)
                            }
                        )
                    }
                }
            }
        },
        modifier = Modifier.padding(10.dp),
    ) {innerPadding ->
        Box ( modifier = Modifier
            .padding(innerPadding)
            .pullRefresh(state) ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                val noticeDataList: List<NoticeData> =
                    uiState.value.noticeDataLists[uiState.value.selectedIndex]
                if (uiState.value.selectedIndex < 2) {
                    // 评论或者点赞
                    items(noticeDataList) { noticeData ->
                        NoticePreviewCard(
                            msg = noticeData,
                            onClickNotice = {onClickPostNotice(noticeData.postId)}
                        )
                        Divider(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        )
                    }
                }
                else{
                    items(noticeDataList) { noticeData ->
                        NoticePreviewCard(
                            msg = noticeData,
                            onClickNotice = { onClickChat(noticeData.noticerID) }
                        )
                        Divider(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        )
                    }
                }
            }
            PullRefreshIndicator(uiState.value.isRefreshing, state, Modifier.align(Alignment.TopCenter))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagePageTopBar() {
    Column(
        Modifier
            .fillMaxWidth()
    ) {
        Text(
            "通知",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 16.dp),
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold)
        )
        Divider(
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}