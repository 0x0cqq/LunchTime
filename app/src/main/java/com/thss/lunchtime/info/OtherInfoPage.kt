package com.thss.lunchtime

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thss.lunchtime.component.InfoComp
import com.thss.lunchtime.component.InfoType
import com.thss.lunchtime.info.OtherInfoPageViewModel
import com.thss.lunchtime.post.PostReviewCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun OtherInfoPage(onClickChat: (userName: String) -> Unit, onClickBack: () -> Unit, onClickPost: (postId: Int) -> Unit, onClickFans : () -> Unit, onClickFollows : () -> Unit, onClickSaved: () -> Unit, otherInfoPageViewModel: OtherInfoPageViewModel = viewModel(), userName: String) {
    val uiState = otherInfoPageViewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        otherInfoPageViewModel.refresh(context, userName)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = uiState.value.infoData.ID + " 的主页")
                },
                navigationIcon = {
                    IconButton(onClick = onClickBack) {
                        Icon(imageVector = Icons.Default.ArrowBack,
                            contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = {otherInfoPageViewModel.onClickBlock(context)}) {
                        Icon(imageVector = Icons.Default.PersonOff,
                            contentDescription = null)
                    }
                },
            )
        }
    ) {
            paddingValues -> Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            InfoComp(
                msg = uiState.value.infoData,
                type = InfoType.Others,
                onClickFollows = onClickFollows,
                onClickFans = onClickFans,
                onClickSaved = onClickSaved,
                onClickRelation = {otherInfoPageViewModel.onClickRelation(context)},
                onClickChat = {
                    onClickChat(uiState.value.infoData.ID)
                }
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Text(text = "TA的动态", fontSize = 14.sp)

            Icon(Icons.Rounded.Sort, contentDescription = null, Modifier.size(18.dp))
        }

        LazyColumn(modifier = Modifier.fillMaxSize()
        ) {
            items(uiState.value.postList) { postData ->
                PostReviewCard(
                    onClickStar = { otherInfoPageViewModel.onClickStar(context, postData.postID) },
                    onClickLike = { otherInfoPageViewModel.onClickLike(context, postData.postID) },
                    onClickTopBar = {},
                    msg = postData,
                    modifier = Modifier.clickable { onClickPost(postData.postID)}
                )
            }
        }

    }
    }
}

@Preview
@Composable
fun OtherInfoPagePreview() {
    OtherInfoPage({}, {}, {}, {}, {}, {}, OtherInfoPageViewModel(),"Other User")
}