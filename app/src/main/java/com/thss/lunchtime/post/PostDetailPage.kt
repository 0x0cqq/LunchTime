package com.thss.lunchtime.post

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thss.lunchtime.LikeBtn
import com.thss.lunchtime.StarBtn
import com.thss.lunchtime.component.CommentComp
import com.thss.lunchtime.component.PostMainBody
import com.thss.lunchtime.component.PostType

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun PostDetailPage(onBack: () -> Unit, onOpenUserInfo: (userName: String) -> Unit, postID: Int, postDetailViewModel: PostDetailViewModel)
{
    val context = LocalContext.current
    val postDetailData = postDetailViewModel.uiState.collectAsState()
    
    
    LaunchedEffect(Unit) {
        postDetailViewModel.refresh(context, postID)
    }
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(text = "动态详情")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(imageVector = Icons.Default.Share,
                            contentDescription = null)
                    }
                },
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
            ) {
                if (postDetailData.value.currentCommentInput.isEmpty()) {
                    LikeBtn(
                        {
                            postDetailViewModel.onClickLike(context, postID)
                        },
                        postDetailData.value.postData.likeCount,
                        postDetailData.value.postData.isLiked
                    )
                    StarBtn(
                        {
                            postDetailViewModel.onClickStar(context, postID)
                        },
                        postDetailData.value.postData.starCount,
                        postDetailData.value.postData.isStared
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // 自定义TextField
                Box(modifier = Modifier.fillMaxWidth(1f)) {
                    BasicTextField(
                        value = postDetailData.value.currentCommentInput,
                        onValueChange = { postDetailViewModel.updateCurrentComment(it) },
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(25)
                            )
                            .height(40.dp)
                            .fillMaxWidth(),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                        decorationBox = { innerTextField ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .animateContentSize { initialValue, targetValue ->  },
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    innerTextField()
                                }
                                IconButton(
                                    onClick = {
                                        postDetailViewModel.sendComment(context)
                                        postDetailViewModel.refresh(context, postID)
                                    }
                                ) {
                                    Icon(
                                        Icons.Filled.Send,
                                        "Send",
                                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            }
                        }
                    )
                }
            }
        },
    ) { paddingValues ->
        Column(modifier = Modifier
        .fillMaxWidth()
        .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                PostMainBody(
                    msg = postDetailData.value.postData,
                    type = PostType(Detailed = true),
                    onClickTopBar = {onOpenUserInfo(postDetailData.value.postData.publisherID)})
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.secondaryContainer)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "评论",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

                Icon(Icons.Rounded.Sort, contentDescription = null, Modifier.size(18.dp))
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(postDetailData.value.commentDataList) { commentData ->
                    Column {
                        CommentComp(msg = commentData)
                        Divider(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        )
                    }
                }
            }
        }
    }
}



//@Preview
//@Composable
//fun PostDetailCardPreview() {
//    PostDetailPage(4, viewModel())
//}