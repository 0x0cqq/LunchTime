package com.thss.lunchtime.component

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.thss.lunchtime.R
import com.thss.lunchtime.ThumbBtn
import java.text.SimpleDateFormat

import java.util.*

data class CommentData(
    val commentAvatar: Uri = Uri.EMPTY,
    val commentID: String = "User_default",
    val commentDate: Date = Date(),
    val commentContent: String = "",
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentComp(msg: CommentData, onClickTopBar: () -> Unit)
{
    Box(
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
                    modifier = Modifier.fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Row (
                        modifier = Modifier.clickable { onClickTopBar() }
                    ){
                        AsyncImage(
                            model = msg.commentAvatar,
                            contentDescription = "Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                // Clip image to shaped as a circle
                                .size(40.dp)
                                .clip(CircleShape),
                        )


                        Spacer(modifier = Modifier.width(8.dp))

                        Column {
                            Text(
                                text = msg.commentID,
                                fontSize = 17.sp
                            )

                            Text(
                                text = SimpleDateFormat("MM-dd HH:mm").format(msg.commentDate),
                                fontSize = 11.sp
                            )

                        }
                    }

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
    CommentComp(
        msg = CommentData(commentContent = "你说得对"),
        onClickTopBar = {}
    )
}