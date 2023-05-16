package com.thss.lunchtime.post

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thss.lunchtime.Like
import com.thss.lunchtime.LikeBtn
import com.thss.lunchtime.Star
import com.thss.lunchtime.StarBtn
import com.thss.lunchtime.component.CommentComp
import com.thss.lunchtime.component.PostMainBody
import com.thss.lunchtime.component.PostType
import com.thss.lunchtime.component.commentData
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun PostDetailedPage(msg: PostData, type: PostType, commentList: ArrayList<commentData>)
{
    var inputText = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(text = "动态详情")
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
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
                if (inputText.value.isEmpty()) {
                    LikeBtn(Like(10, false))

                    StarBtn(Star(10, false))
                }

                Spacer(modifier = Modifier.width(8.dp))

                // 自定义TextField
                Box(modifier = Modifier.fillMaxWidth(1f)) {
                    BasicTextField(
                        value = inputText.value,
                        onValueChange = { inputText.value = it },
                        modifier = Modifier
                            .background(Color.White, CircleShape)
                            .height(40.dp)
                            .fillMaxWidth(),
                        decorationBox = {
                            innerTextField -> Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        ) {
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                innerTextField()
                            }
                            IconButton(
                                onClick = { },
                            ) {
                                Icon(Icons.Filled.Send, null)
                            }
                        }
                        }
                    )
                }
            }
        },
    ) {
        paddingValues -> Column(modifier = Modifier
        .fillMaxWidth()
        .padding(paddingValues)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp, 5.dp, 5.dp, 0.dp)
            ) {
                PostMainBody(msg = msg, type = type)
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(text = "评论", fontSize = 14.sp)

                Icon(Icons.Rounded.Sort, contentDescription = null, Modifier.size(18.dp))
            }

            LazyColumn(modifier = Modifier.fillMaxSize()
            ) {
                items(commentList) { commentData ->
                    CommentComp(msg = commentData)
                }
            }
        }
    }
}

val commentArray = arrayListOf(
    commentData(
        commentContent = "这是一个评论"
    )
)

@Preview
@Composable
fun PostDetailedCardPreview() {
    PostDetailedPage(
        msg = PostData(
            Type = 3,
            graphResources = listOf(),
        ),
        type = PostType(Detailed = true),
        commentList = commentArray
    )
}