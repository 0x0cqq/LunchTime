package com.thss.lunchtime.mediaplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.origeek.imageViewer.viewer.ImageViewer
import com.origeek.imageViewer.viewer.rememberViewerState
import kotlinx.coroutines.launch

@Composable
fun ImagePlayPage(url: String, onBack: () -> Unit) {
    val scope = rememberCoroutineScope()
    val state = rememberViewerState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center,
    ) {
        ImageViewer(
            state = state,
            model = rememberCoilImagePainter(url.toUri()),
            modifier = Modifier.fillMaxSize(),
            detectGesture = {
                onTap = {
                    onBack()
                }
                onDoubleTap = {
                    scope.launch {
                        state.toggleScale(it)
                    }
                }
            },
        )
    }
}

@Composable
fun rememberCoilImagePainter(image: Any): Painter {
    // 加载图片
    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(image)
        .size(coil.size.Size.ORIGINAL)
        .build()
    // 获取图片的初始大小
    return rememberAsyncImagePainter(imageRequest)
}
