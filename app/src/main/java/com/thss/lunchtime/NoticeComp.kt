package com.thss.lunchtime

import androidx.compose.runtime.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ModeComment
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date

data class noticeData(
    val noticerAvatar: String = "User_default",
    val noticerID: String = "User_default",
    val noticeDate: Date = Date(),
    val noticeType: Int = -1,
    val reply: String = "",
    val refData: refData = refData(),
)

data class refData(
    val graphResources : Array<Int> = arrayOf(),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun noticePreviewCard(msg: noticeData)
{
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp)
        ) {
            Column (modifier = Modifier.weight(1f)) {
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row {
                        Image(
                            painter = painterResource(id = R.drawable.touxaingnvhai),
                            contentDescription = "heading",
                            modifier = Modifier
                                // Set image size to 40dp
                                .size(40.dp)
                                // Clip image to shaped as a circle
                                .clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Column {
                            Text(
                                text = msg.noticerID,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )

                            val text = when (msg.noticeType) {
                                1 -> "对你发表了评论"
                                2 -> "点赞了你的帖子"
                                3 -> msg.reply
                                else -> ""
                            }

                            Text(
                                text = text,
                                fontSize = 11.sp
                            )

                        }
                    }

                    if (msg.noticeType == 3) {
                        Text(text = SimpleDateFormat("MM-dd HH:mm").format(msg.noticeDate))
                    } else {
                        Text(text = SimpleDateFormat("MM-dd").format(msg.noticeDate))
                    }

                }

                if (msg.noticeType == 1 && msg.reply != "") {
                    Text(
                        text = msg.reply,
                        Modifier.padding(8.dp, 8.dp, 8.dp, 0.dp)
                    )
                }
            }

            if (msg.refData.graphResources.isNotEmpty()) {
                Image(
                    painter = painterResource(id = msg.refData.graphResources[0]),
                    contentDescription = "heading",
                    modifier = Modifier
                        // Clip image to shaped as a circle
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .padding(start = 8.dp)
                        .aspectRatio(1f)
                        .height(IntrinsicSize.Min)
                )
            }
        }
    }
}


@Preview
@Composable
fun NoticeComment() {
    noticePreviewCard(msg =
    noticeData(noticeType = 1, reply = "你说得对")
    )
}

@Preview
@Composable
fun NoticeLike() {
    noticePreviewCard(msg =
    noticeData(
        noticeType = 2,
        refData = refData(graphResources = arrayOf(R.drawable.wp, R.drawable.wp, R.drawable.wp)),
    )
    )
}

@Preview
@Composable
fun NoticeChat() {
    noticePreviewCard(msg =
    noticeData(noticeType = 3, reply = "你说得对")
    )
}