package com.thss.lunchtime.component

import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ModeComment
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.thss.lunchtime.Like
import com.thss.lunchtime.LikeBtn
import com.thss.lunchtime.R
import com.thss.lunchtime.Star
import com.thss.lunchtime.StarBtn
import com.thss.lunchtime.post.PostData
import com.thss.lunchtime.ui.theme.Purple40
import java.text.SimpleDateFormat

data class PostType(
    val Detailed: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostMainBody(msg: PostData, type: PostType)
{
    Column (modifier = Modifier.padding(bottom = 5.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp)
        ) {
            Row (
                modifier = Modifier.padding(all = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.touxaingnvhai),
                    contentDescription = "heading",
                    modifier = Modifier
                        // Set image size to 40dp
                        .size(40.dp)
                        // Clip image to shaped as a circle
                        .clip(CircleShape)
                )
                // Add a horizontal space between the image and the column
                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(text = msg.publisherID)
                    // Add a vertical space between the publisher and date
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = SimpleDateFormat("yyyy-MM-dd HH:mm").format(msg.publishDate))
                }
            }

            if (type.Detailed) {
                val icon = when (msg.publisherStatus) {
                    1 -> Icons.Rounded.PersonAdd
                    2 -> Icons.Rounded.HowToReg
                    3 -> Icons.Rounded.PersonOff
                    else -> Icons.Rounded.PersonAdd
                }
                Icon(icon, contentDescription = null, tint = Purple40)
            }
        }

        Column (modifier = Modifier.padding(8.dp)) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val color = when (msg.Type) {
                    1 -> Color.Blue
                    2 -> Color.Red
                    3 -> Color.Yellow
                    else -> Color.Gray
                }

                // Tag
                Card(
                    shape = RoundedCornerShape(15.dp),
                    colors = CardDefaults.cardColors(containerColor = color),
                ) {
                    Text(
                        text = msg.tag,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 1.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // title
                Text(
                    text = msg.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(bottom = 4.dp)
                )
            }

            var expandcontent = remember {
                mutableStateOf(false)
            }

            // content
            Box(modifier = Modifier.animateContentSize(animationSpec = tween(durationMillis = 100) )) {
                Text(
                    text = msg.content,
                    maxLines = if (expandcontent.value) 100 else 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.clickable { expandcontent.value = !expandcontent.value }
                )
            }
        }

        // image show
        AsyncPostPhotoGrid(imageUris = msg.graphResources, columnCount = 3)

        // location Tag
        Card(
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(containerColor = Color.LightGray),
            modifier = Modifier.padding(start = 12.dp)
        ) {
            Text(
                text = msg.tag,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 1.dp),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun LikeStarComment(msg: PostData) {
    // bottom like, star, comment
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LikeBtn(like = Like(10, false))

        StarBtn(star = Star(10, false))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.ModeComment, "Comment", tint = Color.Gray)
            }
            Text(text = msg.commentCount.toString())
        }

    }
}

@Composable
fun <T> Grid(
    data: List<T>,
    columnCount: Int,
    modifier: Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    itemContent: @Composable BoxScope.(index: Int, content: T) -> Unit,
) {
    val size = data.count()
    val rows = if (size == 0) 0 else 1 + (size - 1) / columnCount
    repeat(rows) { rowIndex ->
        Row(
            horizontalArrangement = horizontalArrangement,
            modifier = modifier
        ) {
            Spacer(modifier = Modifier.width(8.dp))

            for (columnIndex in 0 until columnCount) {
                val itemIndex = rowIndex * columnCount + columnIndex
                if (itemIndex < size) {
                    Box(
                        modifier = Modifier.weight(1F, fill = true),
                        propagateMinConstraints = true
                    ) {
                        itemContent(itemIndex, data[itemIndex])
                    }
                } else {
                    Spacer(Modifier.weight(1F, fill = true))
                }
            }

            Spacer(modifier = Modifier.width(8.dp))
        }

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(4.dp))
    }
}

@Composable
fun AsyncPostPhotoGrid(columnCount: Int, imageUris: List<Uri>, modifier: Modifier = Modifier) {
    Grid(
        data = imageUris,
        columnCount = columnCount,
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) { _, imageUri ->
        SubcomposeAsyncImage(
            model = imageUri,
            contentDescription = "post image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.aspectRatio(1F),
            alignment = Alignment.Center
        ) {
            val state = painter.state
            if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                CircularProgressIndicator()
            } else {
                SubcomposeAsyncImageContent()
            }
        }
    }
}

@Preview
@Composable
fun PostBodyPreview() {
    PostMainBody(
        msg = PostData(
            content = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
            Type = 3,
            graphResources = listOf()
        ),
        type = PostType(Detailed = true)
    )
}