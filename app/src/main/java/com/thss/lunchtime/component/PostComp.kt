package com.thss.lunchtime.component

import android.net.Uri
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.thss.lunchtime.CommentBtn
import com.thss.lunchtime.LikeBtn
import com.thss.lunchtime.StarBtn
import com.thss.lunchtime.post.PostData
import com.thss.lunchtime.ui.theme.Purple40
import kotlinx.serialization.json.Json
import me.onebone.parvenu.ParvenuString
import me.onebone.parvenu.toAnnotatedString
import java.text.SimpleDateFormat
import java.util.Locale

data class PostType(
    val Detailed: Boolean = false
)

private val json = Json { ignoreUnknownKeys = true }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostMainBody(msg: PostData, type: PostType, onClickTopBar: () -> Unit, onClickVideo:(uri: String)->Unit)
{
    Column (modifier = Modifier.padding(bottom = 5.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp)
                .clickable{
                    onClickTopBar()
                }
        ) {
            Row (
                modifier = Modifier.padding(all = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AsyncImage(
                    model = msg.publisherAvatar,
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        // Clip image to shaped as a circle
                        .size(40.dp)
                        .clip(CircleShape),
                )
//                Image(
//                    painter = painterResource(id = R.drawable.touxaingnvhai),
//                    contentDescription = "heading",
//                    modifier = Modifier
//                        // Set image size to 40dp
//                        .size(40.dp)
//                        // Clip image to shaped as a circle
//                        .clip(CircleShape)
//                )
                // Add a horizontal space between the image and the column
                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(text = msg.publisherID)
                    // Add a vertical space between the publisher and date
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINESE).format(msg.publishDate))
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

                if(msg.tag.isNotEmpty()) {
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
                }

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

            val expandContent = remember {
                mutableStateOf(false)
            }

            // content
            Box(modifier = Modifier.animateContentSize(animationSpec = tween(durationMillis = 100) )) {
                val content : ParvenuString = try {
                    json.decodeFromString(msg.content)
                } catch (e: Exception) {
                    Log.d("PostMainBody", "json decode error: ${e.message}")
                    ParvenuString(msg.content)
                }
                if(content.text.isNotEmpty()) {
                    Text(
                        text = content.toAnnotatedString(),
                        maxLines = if (expandContent.value) 100 else 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.clickable { expandContent.value = !expandContent.value }
                    )
                }
            }
        }
    }

    // image show
    AsyncPostPhotoGrid(
        imageUris = msg.graphResources,
        videoUris = msg.videoResources,
        columnCount = 3,
        isVideo = msg.isVideo,
        openImage = onClickVideo,
    )

    // location Tag
    if(type.Detailed && msg.location.isNotEmpty()) {
        Card(
            shape = RoundedCornerShape(50),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier.padding(start = 12.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
            ) {
                Icon(
                    Icons.Rounded.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = msg.location,
                    fontSize = MaterialTheme.typography.labelSmall.fontSize,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun LikeStarComment(onClickLike: () -> Unit, onClickStar: () -> Unit, msg: PostData) {
    // bottom like, star, comment
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LikeBtn(onClickLike = onClickLike, msg.likeCount, msg.isLiked)
        StarBtn(onClickStar = onClickStar, msg.starCount, msg.isStared)
        CommentBtn(onClickComment = {/* TODO */}, msg.commentCount)
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
fun AsyncPostPhotoGrid(columnCount: Int, imageUris: List<Uri>, videoUris: List<Uri>, isVideo: Boolean, openImage: (url : String) -> Unit ,modifier: Modifier = Modifier) {
    Grid(
        data = imageUris,
        columnCount = columnCount,
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) { _, imageUri ->
        Box{
            if(isVideo){
                SubcomposeAsyncImage(
                    model = imageUri,
                    contentDescription = "post image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.
                        aspectRatio(1F)
                        .clickable { openImage(videoUris[0].toString()) },
                    alignment = Alignment.Center
                ) {
                    val state = painter.state
                    if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                        CircularProgressIndicator()
                    } else {
                        SubcomposeAsyncImageContent()
                    }
                }
                Icon(
                    imageVector = Icons.Rounded.PlayCircleFilled,
                    contentDescription = "Play",
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5F))
                        .align(Alignment.Center)
                )
            } else {
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
        type = PostType(Detailed = true),
        onClickTopBar = {},
        onClickVideo = {},
    )
}