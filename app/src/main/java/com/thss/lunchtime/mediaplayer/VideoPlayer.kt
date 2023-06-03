package com.thss.lunchtime.mediaplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.halilibo.composevideoplayer.VideoPlayer
import com.halilibo.composevideoplayer.VideoPlayerSource
import com.halilibo.composevideoplayer.rememberVideoPlayerController

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun VideoPlayPage(url: String, onBack: () -> Unit) {
    val videoPlayerController = rememberVideoPlayerController()


    LaunchedEffect(url) {
        videoPlayerController.setSource(VideoPlayerSource.Network(url))
    }

    VideoPlayer(
        videoPlayerController = videoPlayerController,
        backgroundColor = Color.Transparent,
        modifier = Modifier.fillMaxWidth(),
        controlsEnabled = true,
    )
}

@Composable
@Preview
fun VideoPlayerPreview(){
    val url = "http://82.156.30.206:8000/media/postVideo/android.mp4"
    VideoPlayPage(url, {})
}