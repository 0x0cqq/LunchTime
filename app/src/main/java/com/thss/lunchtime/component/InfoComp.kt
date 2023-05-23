package com.thss.lunchtime.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.rounded.HowToReg
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material.icons.rounded.PersonOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thss.lunchtime.R
import com.thss.lunchtime.ui.theme.Purple40
import java.util.*

data class InfoData(
    val Avatar: String = "User_default",
    val ID: String = "User_default",
    val SelfIntro: String = "Hello World",
    val followCnt: Int = 0,
    val fansCnt: Int = 0,
    val relation: Int = 2, //1: unfollow, 2: followed, 3:blocked
)

data class InfoType(
    val infoType: Int = 1, //1: self, 2: others
) {
    companion object {
        val Self: InfoType
            get() = InfoType(1)

        val Others: InfoType
            get() = InfoType(2)
    }
}

@Composable
fun InfoComp(msg: InfoData, type: InfoType)
{
    Column (modifier = Modifier.padding(all = 16.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.touxaingnvhai),
                contentDescription = "heading",
                modifier = Modifier
                    // Set image size to 40dp
                    .size(100.dp)
                    // Clip image to shaped as a circle
                    .clip(CircleShape)
            )

            Row (verticalAlignment = Alignment.CenterVertically) {
                if (type.infoType == 2) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            Icons.Outlined.Chat,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(25.dp))
                    }

                    val icon = when (msg.relation) {
                        1 -> Icons.Rounded.PersonAdd
                        2 -> Icons.Rounded.HowToReg
                        3 -> Icons.Rounded.PersonOff
                        else -> Icons.Rounded.PersonAdd
                    }
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = Purple40,
                        modifier = Modifier.size(35.dp)
                    )
                }
            }
        }

        Text(
            text = msg.ID,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        // Add a vertical space between the publisher and date
        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = msg.SelfIntro,
            fontSize = 18.sp
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp, top = 16.dp)
        ) {
            Text(text = msg.followCnt.toString() + "  关注")
            Spacer(modifier = Modifier.width(24.dp))
            Text(text = msg.fansCnt.toString() + "  粉丝")
            Spacer(modifier = Modifier.width(24.dp))
            // 可以考虑替换成 when 语句
            if (type.infoType == 2) {
                Text(text = "TA的收藏")
            } else if (type.infoType == 1) {
                Text(text = "我的收藏")
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoPreviewComp(msg: InfoData)
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
                                text = msg.ID,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )

                            Text(
                                text = msg.SelfIntro,
                                fontSize = 12.sp
                            )

                            Text(
                                text = msg.fansCnt.toString() + "  粉丝",
                                fontSize = 12.sp
                            )
                        }
                    }

                    val icon = when (msg.relation) {
                        1 -> Icons.Rounded.PersonAdd
                        2 -> Icons.Rounded.HowToReg
                        3 -> Icons.Rounded.PersonOff
                        else -> Icons.Rounded.PersonAdd
                    }
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = if (msg.relation == 3) Color.Gray else Purple40)
                }
            }
        }
    }
}


@Preview
@Composable
fun InfoPreview() {
    InfoComp(
        msg = InfoData(),
        type = InfoType(1)
    )
}

@Preview
@Composable
fun InfoPreviewPreview() {
    InfoPreviewComp(
        msg = InfoData(relation = 3)
    )
}