package com.thss.lunchtime.newpost

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaRecorder.VideoEncoder
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.outlined.Tag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thss.lunchtime.R
import com.thss.lunchtime.common.LocationUtils
import com.thss.lunchtime.component.Grid
import kotlinx.serialization.descriptors.PrimitiveKind
import me.onebone.parvenu.ParvenuEditor
import me.onebone.parvenu.ParvenuSpanToggle
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun NewPostPage(onClickBack: () -> Unit = {}, onClickSend : ( newPostData : NewPostData) -> Unit = { _ -> }, newPostViewModel: NewPostViewModel = viewModel()) {
    val uiState = newPostViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.touxaingnvhai),
                        contentDescription = "heading",
                        modifier = Modifier
                            .size(40.dp)
                            // Clip image to shaped as a circle
                            .clip(CircleShape)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onClickBack,
                    ) {
                       Icon(
                           Icons.Outlined.Close,
                           contentDescription = "Cancel",
                       )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            onClickSend(uiState.value)
                        },
                    ) {
                        Icon(
                            Icons.Outlined.Send,
                            contentDescription = "Send",
                        )
                    }
                }
            )
        },
        bottomBar = {
            NewPostBottomBar(newPostViewModel = newPostViewModel)
        }
    ) { paddingValues ->
        NewPostContent(
            newPostViewModel = newPostViewModel,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconCard(
        icon: ImageVector, text: String,
        onClick: () -> Unit, isUsed : Boolean, onClear: () -> Unit,
        modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(enabled = true) { onClick() },
        shape = RoundedCornerShape(100),
//        colors = if(isUsed) {
//            CardDefaults.cardColors(
//                contentColor = MaterialTheme.colorScheme.onPrimary
//            )
//        } else {
//            CardDefaults.cardColors(
//                contentColor = MaterialTheme.colorScheme.onSurface
//            )
//        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Icon(
                icon,
                contentDescription = text,
                modifier = Modifier.wrapContentHeight()
            )
            Spacer(
                modifier = Modifier.width(4.dp)
            )
            Text(
                text = text
            )
            if(isUsed) {
                Spacer(
                    modifier = Modifier.width(4.dp)
                )
                Icon(
                    Icons.Outlined.Close,
                    contentDescription = "Clear",
                    modifier = Modifier.wrapContentHeight().clickable { onClear() }
                )
            }
        }
    }
}

@Preview
@Composable
fun IconCardPreview() {
    IconCard(
        icon = Icons.Outlined.MyLocation,
        text = "Location?",
        onClick = {},
        isUsed = true,
        onClear = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPostBottomBar(newPostViewModel: NewPostViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val locationPermissionRequest = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Fine location access granted.
                newPostViewModel.setLocation(LocationUtils.getCurrentLocation(context)!!.latitude.toInt().toString()
                    + " "
                    + LocationUtils.getCurrentLocation(context)!!.longitude.toInt().toString())
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
            } else -> {
                // No location access granted.

            }
        }
    }
    val uiState = newPostViewModel.uiState.collectAsState()
    val openDialog = remember { mutableStateOf(false) }

    // dialog
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text("Dismiss")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            title = {
                Text("Choose a tag")
            },
            text = {
                TextField(
                    value = uiState.value.tag,
                    onValueChange = {
                        newPostViewModel.setTag(it)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    }

    Box(
        modifier = modifier.fillMaxWidth().height(50.dp).padding(horizontal = 12.dp)
    ) {
        // location card
        IconCard(
            icon = Icons.Outlined.MyLocation,
            text =
                if (uiState.value.isLocationUsed) uiState.value.location
                else "定位",
            onClick = {
                locationPermissionRequest.launch(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                )
            },
            modifier = Modifier.align(Alignment.CenterStart),
            isUsed = uiState.value.isLocationUsed,
            onClear = {
                newPostViewModel.setLocation("")
            },
        )
        // tag card
        IconCard(
            icon = Icons.Outlined.Tag,
            text =
                if (uiState.value.isTagUsed) uiState.value.tag
                else "TAG",
            onClick = {
//                newPostViewModel.setTag("HIHIHI tag")
                openDialog.value = true
            },
            modifier = Modifier.align(Alignment.CenterEnd),
            isUsed = uiState.value.isTagUsed,
            onClear = {
                newPostViewModel.setTag("")
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPostContent(newPostViewModel: NewPostViewModel, modifier: Modifier = Modifier) {
    val uiState = newPostViewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val mostImages = 9
    val launcher =
        if (mostImages - uiState.value.selectedImgUris.size <= 1) {
            rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if(uri != null) {
                    val inputStream = context.contentResolver.openInputStream(uri)
                    newPostViewModel.appendImages(
                        listOf(
                            BitmapFactory.decodeStream(inputStream).asImageBitmap()
                        )
                    )
                }
            }
        } else if (uiState.value.selectedImgUris.isEmpty()){
            rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {uri ->
                if (uri != null){
                    val contentType = context.contentResolver.getType(uri)
                    if (contentType?.startsWith("image/") == true) {
                        // 处理图片
                        val inputStream = context.contentResolver.openInputStream(uri)
                        newPostViewModel.appendImages(
                            listOf(
                                BitmapFactory.decodeStream(inputStream).asImageBitmap()
                            )
                        )
                    } else if (contentType?.startsWith("video/") == true){
                        // 处理视频
                        val inputStream = context.contentResolver.openInputStream(uri)
                        var fileOutputStream: FileOutputStream? = null
                        // 获得缩略图
                        val thumbnail: Bitmap = context.contentResolver.loadThumbnail(uri, Size(640,480), null)
                        newPostViewModel.appendImages(
                            listOf(thumbnail.asImageBitmap())
                        )
                        // 保存视频流
                        // 从uri得到文件
                        Log.d("Video", "start getting file from uri")
                        try{
                            // 获得文件名
                            var videoName: String = "";
                            val cursor = context.contentResolver.query(uri, null, null, null, null)
                            cursor?.let {
                                if (it.moveToFirst()) {
                                    videoName = it.getString(it.getColumnIndexOrThrow("_display_name"))
                                    cursor.close()
                                }
                            }
                            // 获得文件路径
                            val videoDir = File(context.getExternalFilesDir(Environment.DIRECTORY_MOVIES), "Videos")
                            if (!videoDir.exists()) {
                                videoDir.mkdirs()
                            }
                            // 写入File
                            val videoFile = File(videoDir, videoName)
                            fileOutputStream = FileOutputStream(videoFile)
                            inputStream?.copyTo(fileOutputStream)
                            // 添加到video列表
                            newPostViewModel.appendVideos(listOf(videoFile))
                        } catch (e: IOException){
                            e.printStackTrace()
                        }
                        // 设置 isVideoFlag
                        newPostViewModel.setVideoFlag(true)
                    }
                }
            }
        }
        else {
            rememberLauncherForActivityResult(
                ActivityResultContracts.PickMultipleVisualMedia(mostImages - uiState.value.selectedImgUris.size)
            ) { uris: List<Uri> ->
                if(uris.isNotEmpty()) {
                    val inputStreams = uris.map { context.contentResolver.openInputStream(it) }
                    newPostViewModel.appendImages(inputStreams.map { BitmapFactory.decodeStream(it).asImageBitmap() })
                }
            }
        }
    LazyColumn(
            modifier = modifier.fillMaxSize()
    ) {
        item {
            TextField(
                value = uiState.value.title,
                textStyle = MaterialTheme.typography.headlineSmall,
                onValueChange = { newPostViewModel.setTitle(it) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = {
                    Text("标题",
                        style = MaterialTheme.typography.headlineSmall,
                    ) },
                keyboardActions = KeyboardActions {
                        focusManager.moveFocus(FocusDirection.Next) },
            )
        }
        item {
            ParvenuEditor(
                value = uiState.value.richContent,
                onValueChange = { newPostViewModel.setRichContent(it) },
            ) { value, onValueChange ->
                TextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "内容",
                        )
                    },
                    keyboardActions = KeyboardActions {
                        focusManager.moveFocus(FocusDirection.Next)
                    },
                )
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ParvenuSpanToggle(
                    value = uiState.value.richContent,
                    onValueChange = { newPostViewModel.setRichContent(it) },
                    spanFactory = { SpanStyle(fontStyle = FontStyle.Italic) },
                    spanEqualPredicate = { style ->
                        style.fontStyle == FontStyle.Italic
                    }
                ) { enabled, onToggle ->
                    Button(
                        modifier = Modifier.alpha(if (enabled) 1f else 0.3f),
                        onClick = { onToggle() }
                    ) {
                        Text(text = "italic")
                    }
                }

                ParvenuSpanToggle(
                    value = uiState.value.richContent,
                    onValueChange = { newPostViewModel.setRichContent(it) },
                    spanFactory = { SpanStyle(fontWeight = FontWeight.Bold) },
                    spanEqualPredicate = { style ->
                        style.fontWeight == FontWeight.Bold
                    }
                ) { enabled, onToggle ->
                    Button(
                        modifier = Modifier.alpha(if (enabled) 1f else 0.3f),
                        onClick = { onToggle() }
                    ) {
                        Text(text = "bold")
                    }
                }
                ParvenuSpanToggle(
                    value = uiState.value.richContent,
                    onValueChange = { newPostViewModel.setRichContent(it) },
                    spanFactory = { SpanStyle(color = Color.Red) },
                    spanEqualPredicate = { style ->
                        style.color == Color.Red
                    }
                ) { enabled, onToggle ->
                    Button(
                        modifier = Modifier.alpha(if (enabled) 1f else 0.3f),
                        onClick = { onToggle() }
                    ) {
                        Text(text = "red")
                    }
                }
            }
        }
        item {
            NewPostPhotoGrid(
                onNewImage = {
                    if (uiState.value.selectedImgUris.isEmpty()){
                        launcher.launch (
                            PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                        )
                    }
                    else{
                        launcher.launch (
                            PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                },
                onOpenImage = { index ->
                    // TODO: image preview page
                },
                onClose = { index ->
                    newPostViewModel.removeImage(index)
                },
                mostImages = mostImages,
                images = uiState.value.selectedImgUris,
                isVideo = uiState.value.isVideo,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Composable
fun NewPostPhotoGrid(
    onNewImage: () -> Unit,
    onOpenImage: (Int) -> Unit,
    onClose : (Int) -> Unit,
    images: List<ImageBitmap>,
    mostImages : Int,
    isVideo : Boolean,
    modifier: Modifier = Modifier) {
    // TODO: change this theme detector with CompositionLocalProvider to improve the performance
    val imagesWithAddButton = if (images.size < mostImages && !isVideo) {
        images + listOf(
            ImageBitmap.imageResource(
                if (isSystemInDarkTheme()) R.drawable.add_photo_dark
                else R.drawable.add_photo_light
            )
        )
    } else {
        images
    }
    Grid(
        data = imagesWithAddButton,
        columnCount = 3,
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) { index, image ->
        Box {
            Image(
                bitmap = image,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.aspectRatio(1F).clickable {
                    if (index == imagesWithAddButton.size - 1) {
                        if (images.size == mostImages || isVideo) {
                            onOpenImage(index)
                        } else {
                            onNewImage()
                        }
                    } else {
                        onOpenImage(index)
                    }
                },
                alignment = Alignment.Center
            )
            if (images.size == mostImages || index != imagesWithAddButton.size - 1 || isVideo) {
                IconButton(
                    onClick = {
                        onClose(index)
                    },
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .padding(4.dp)
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5F))
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Remove",
                    )
                }
            }
        }
    }
}