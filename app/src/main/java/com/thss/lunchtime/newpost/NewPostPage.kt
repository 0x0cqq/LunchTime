package com.thss.lunchtime.newpost

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.widget.Toast
import android.os.Environment
import android.util.Log
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.outlined.Tag
import androidx.compose.material.icons.rounded.PlayCircleFilled
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.thss.lunchtime.R
import com.thss.lunchtime.common.LocationInfo
import com.thss.lunchtime.common.LocationUtils
import com.thss.lunchtime.component.Grid
import kotlinx.serialization.descriptors.PrimitiveKind
import com.thss.lunchtime.data.userPreferencesStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun NewPostPage(onClickBack: () -> Unit = {},
                onClickSend : ( newPostData : NewPostData) -> Unit = { _ -> },
                newPostViewModel: NewPostViewModel = viewModel()) {
    val uiState = newPostViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val userData = context.userPreferencesStore
    val isSendButtonDisabled = remember { mutableStateOf(false) }
    val sendPostScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        newPostViewModel.initData(context, userData.data.first().userName)
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    AsyncImage(
                        uiState.value.avatarUri,
                        contentDescription = "avatar",
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
                        enabled = !isSendButtonDisabled.value,
                        onClick = {
                            onClickSend(uiState.value)
                            isSendButtonDisabled.value = true
                            sendPostScope.launch {
                                delay(5000)
                                isSendButtonDisabled.value = false
                            }
                        },
                    ) {
                        Icon(
                            Icons.Outlined.Send,
                            contentDescription = "Send",
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                )
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
                    modifier = Modifier
                        .wrapContentHeight()
                        .clickable { onClear() }
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
    val openLocationDialog = remember { mutableStateOf(false) }
    val locationAddressList = remember { mutableStateOf(listOf<LocationInfo>()) }
    // location dialog
    if (openLocationDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openLocationDialog.value = false
            },
            dismissButton = {
                Button(
                    onClick = { openLocationDialog.value = false }
                ) {
                    Text("取消")
                }
            },
            confirmButton = {},
            title = { Text("请选择一个位置") },
            text = {
                LazyColumn(
                    modifier = Modifier.height(200.dp)
                ) {
                    items(locationAddressList.value) { addressInfo ->
                        ListItem(
                            headlineText = { Text(addressInfo.name) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    newPostViewModel.setLocation(addressInfo.name)
                                    openLocationDialog.value = false
                                },
                            trailingContent = {
                                Text("距您 ${addressInfo.distance.roundToInt()} 米")
                            }
                        )
                    }
                }
            }
        )
    }
    val locationPermissionRequest = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        Log.d("LunchTime Location", "Enter permission")
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Fine location access granted.
                Log.d("LunchTime Location", "Fine location access granted.")
                LocationUtils.getGeoFromLocation(context) { currentLocation, addressList ->
                    Log.d("LunchTime Location", "Get location, enter listener")
                    // get location list
                    if(currentLocation != null) {
                        val tmpDistance = FloatArray(3)
                        var distanceList = arrayOf<Double>()
                        for (address in addressList) {
                            Location.distanceBetween(currentLocation.latitude, currentLocation.longitude,
                                address.latitude, address.longitude,
                                tmpDistance
                            )
                            distanceList = distanceList.plus(tmpDistance[0].toDouble())
                        }
                        Log.d("LunchTime Location", "Get location, distance list: ${distanceList.size}")
                        locationAddressList.value = addressList.mapIndexed { index, address ->
                            var featureName = ""
//                            featureName = featureName.plus(address.countryName ?: "")
//                            featureName = featureName.plus(address.adminArea ?: "")
//                            featureName = featureName.plus(address.subAdminArea ?: "")
                            featureName = featureName.plus(address.locality ?: "")
                            featureName = featureName.plus(address.subLocality ?: "")
                            featureName = featureName.plus(address.thoroughfare ?: "")
                            featureName = featureName.plus(address.subThoroughfare ?: "")
                            featureName = featureName.plus(address.featureName ?: "")
                            if (featureName == "") {
                                featureName = "${String.format("%.2f", address.latitude)}, ${String.format("%.2f", address.latitude)}"

                            }
                            LocationInfo(
                                featureName,
                                distanceList[index]
                            )
                        }
                        // open the dialog
                        openLocationDialog.value = true
                    } else {
                        Toast.makeText(context, "获取位置失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
                Toast.makeText(context, "请授予精确位置", Toast.LENGTH_SHORT).show()
            }
            else -> {
                // No location access granted.
                Toast.makeText(context, "未授予位置权限", Toast.LENGTH_SHORT).show()
            }
        }
    }
    val uiState = newPostViewModel.uiState.collectAsState()
    val openTagDialog = remember { mutableStateOf(false) }

    val radioOptions = listOf("无", "校园活动", "失物招领", "随便聊聊")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

    // tag dialog
    if (openTagDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openTagDialog.value = false
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openTagDialog.value = false
                    }
                ) {
                    Text("取消")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        openTagDialog.value = false
                        if (selectedOption == "无") {
                            uiState.value.isTagUsed = false
                        } else {
                            uiState.value.isTagUsed = true
                            uiState.value.tag = selectedOption
                        }
                    }
                ) {
                    Text("确定")
                }
            },
            title = {
                Text("选择一个标签")
            },
            text = {
                Column(Modifier.selectableGroup()) {
                    radioOptions.forEach { text ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .selectable(
                                    selected = (text == selectedOption),
                                    onClick = { onOptionSelected(text) },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (text == selectedOption),
                                onClick = null // null recommended for accessibility with screen readers
                            )
                            Text(
                                text = text,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            }
        )
    }

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),

    ) {
        // location card
        item {
            IconCard(
                icon = Icons.Outlined.MyLocation,
                text =
                if (uiState.value.isLocationUsed) uiState.value.location
                else "点击添加定位",
                onClick = {
                    locationPermissionRequest.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                },
                isUsed = uiState.value.isLocationUsed,
                onClear = {
                    newPostViewModel.setLocation("")
                },
            )
        }
        // tag card
        item {
            IconCard(
                icon = Icons.Outlined.Tag,
                text =
                if (uiState.value.isTagUsed) uiState.value.tag
                else "点击添加标签",
                onClick = {
                    openTagDialog.value = true
                },
                isUsed = uiState.value.isTagUsed,
                onClear = {
                    newPostViewModel.setTag("")
                    onOptionSelected("无")
                }
            )
        }
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
                            var videoName: String = ""
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
            NewPostTitleContent(
                newPostViewModel,
                focusManager
            )
        }

        item {
            Spacer(modifier = Modifier.height(64.dp))
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
                modifier = Modifier
                    .aspectRatio(1F)
                    .clickable {
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
                if (isVideo){
                    Icon(
                        imageVector = Icons.Rounded.PlayCircleFilled,
                        contentDescription = "Play",
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5F))
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
}