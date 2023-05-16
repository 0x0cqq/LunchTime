package com.thss.lunchtime

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat

import java.util.*

data class commentData(
    val commentAvatar: String = "User_default",
    val commentID: String = "User_default",
    val commentDate: Date = Date(),
    val commentContent: String = "",
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentComp(msg: commentData)
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
                                text = msg.commentID,
                                fontSize = 15.sp
                            )

                            Text(
                                text = SimpleDateFormat("MM-dd").format(msg.commentDate),
                                fontSize = 11.sp
                            )

                        }
                    }

                    // likebtn
                    ThumbBtn()

                }

                Text(
                    text = msg.commentContent,
                    Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}


@Preview
@Composable
fun CommentCompPreview() {
    CommentComp(msg =
        commentData(commentContent = "你说得对")
    )
}