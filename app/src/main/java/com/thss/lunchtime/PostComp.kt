package com.thss.lunchtime

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ModeComment
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date

data class PostData(
    val publisherAvatar: String = "User_default",
    val publisherID: String = "User_default",
    val publishDate: Date = Date(),
    val postID: Int = 0,
    val title: String = "title",
    val content: String = "content",
    val commentCnt: Int = 0,
    var likeCnt: Int = 0,
    val starCnt: Int = 0,
    var isLiked: Boolean = false,
    val Type: Int = -1,
    val Tag: String = "Tag",
    val graphResources : Array<Int> = arrayOf(),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostPreviewCard(msg: PostData)
{
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Column {
            Row (modifier = Modifier.padding(all = 8.dp)) {
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
                            text = msg.Tag,
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

                // content
                Text(text = msg.content)
            }

            // image show
            val imagesResources : List<ImageBitmap> =
                msg.graphResources.map {
                    ImageBitmap.imageResource(id = it)
                }
            PostPhotoGrid(images = imagesResources, columnCount = 3)

            // location Tag
            Card(
                shape = RoundedCornerShape(15.dp),
                colors = CardDefaults.cardColors(containerColor = Color.LightGray),
                modifier = Modifier.padding(start = 12.dp)
            ) {
                Text(
                    text = msg.Tag,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 1.dp),
                    fontSize = 12.sp
                )
            }

            // bottom like, star, comment
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Filled.FavoriteBorder, "Liked")
                    }
                    Text(text = msg.likeCnt.toString())
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Filled.StarBorder, "Stared")
                    }
                    Text(text = msg.starCnt.toString())
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Filled.ModeComment, "Comment")
                    }
                    Text(text = msg.commentCnt.toString())
                }

            }

        }
    }
}

@Composable
fun <T> Grid(
    data: List<T>,
    columnCount: Int,
    modifier: Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    itemContent: @Composable BoxScope.(T) -> Unit,
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
                        itemContent(data[itemIndex])
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
fun PostPhotoGrid(columnCount: Int, images: List<ImageBitmap>, modifier: Modifier = Modifier) {
    Grid(
        data = images,
        columnCount = columnCount,
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Image(
            bitmap = it,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.aspectRatio(1F),
            alignment = Alignment.Center
        )
    }
}

@Preview
@Composable
fun PostPreviewCardPreview() {
    PostPreviewCard(
        msg = PostData(
            Type = 3,
            graphResources = arrayOf(R.drawable.wp, R.drawable.wp, R.drawable.wp)
        )
    )
}
