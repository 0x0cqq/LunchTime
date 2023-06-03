package com.thss.lunchtime.mediaplayer

import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.MediaController
import android.widget.SeekBar
import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import kotlinx.coroutines.delay

@Composable
fun VideoPlayPage(uri: Uri) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5F))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        VideoPlayer(uri = uri)
    }
}

@Composable
fun VideoPlayer(uri: Uri) {
    val context = LocalContext.current
    val videoView = remember { VideoView(context) }
    val mediaController = remember { MediaController(context) }
    val seekBarProgress = remember { mutableStateOf(0) }

    AndroidView(
        factory = { context ->
            videoView.setVideoURI(uri)
            videoView.setMediaController(mediaController)
            videoView.start()
            videoView.setOnPreparedListener { mediaPlayer ->
                seekBarProgress.value = mediaPlayer.currentPosition
            }
            videoView
        }
    )

    LaunchedEffect(Unit) {
        while (true) {
            seekBarProgress.value = videoView.currentPosition
            delay(500)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { context ->
                val seekBar = SeekBar(context)
                seekBar.max = videoView.duration

                seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        if (fromUser) {
                            videoView.seekTo(progress)
                        }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                })

                seekBar.progress = seekBarProgress.value

                seekBar
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}

@Composable
@Preview
fun VideoPlayerPreview(){
    val uri = "http://82.156.30.206:8000/media/postVideo/android.mp4".toUri()
    VideoPlayPage(uri)
}
//
//import android.net.Uri
//import android.os.Handler
//import android.os.Looper
//import android.util.Log
//import android.widget.MediaController
//import android.widget.SeekBar
//import android.widget.VideoView
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.core.net.toUri
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.withContext
//import java.io.File
//import java.io.FileOutputStream
//import java.net.URL
//import java.net.URLEncoder
//
//@Composable
//fun VideoPlayPage(uri: Uri) {
//    val videoPath = remember { mutableStateOf("") }
//    val downloading = remember { mutableStateOf(true) }
//
//    LaunchedEffect(Unit) {
//        try {
//            val localPath = "path/to/local/video.mp4"
//            downloadVideo(uri.toString(), localPath)
//            videoPath.value = localPath
//        } catch (e: Exception) {
//            // 处理下载异常
//        } finally {
//            downloading.value = false
//        }
//    }
//
//    if (downloading.value) {
//        Log.d("Video", "download now...")
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            CircularProgressIndicator()
//        }
//    } else {
//        if (videoPath.value.isNotEmpty()) {
//            Box(modifier = Modifier.fillMaxSize()) {
//                VideoPlayer(videoPath.value)
//            }
//        } else {
//            // 显示错误信息
//        }
//    }
//
//    // 其他UI组件...
//}
//
//@Composable
//fun VideoPlayer(url: String) {
//    val context = LocalContext.current
//    val videoView = remember { VideoView(context) }
//    val mediaController = remember { MediaController(context) }
//    val seekBarProgress = remember { mutableStateOf(0) }
//
//    DisposableEffect(Unit) {
//        onDispose {
//            videoView.stopPlayback()
//        }
//    }
//
//    AndroidView(
//        factory = { context ->
//            videoView.setVideoPath(url)
////            val encodedUri = URLEncoder.encode(uri.toString(), "UTF-8")
////            videoView.setVideoURI(Uri.parse(encodedUri))
//            videoView.setMediaController(mediaController)
//            videoView.start()
//            videoView.setOnPreparedListener { mediaPlayer ->
//                seekBarProgress.value = mediaPlayer.currentPosition
//            }
//            videoView
//        }
//    )
//
//    LaunchedEffect(Unit) {
//        while (true) {
//            seekBarProgress.value = videoView.currentPosition
//            delay(500)
//        }
//    }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        AndroidView(
//            factory = { context ->
//                val seekBar = SeekBar(context)
//                seekBar.max = videoView.duration
//
//                seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                        if (fromUser) {
//                            videoView.seekTo(progress)
//                        }
//                    }
//
//                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
//
//                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
//                })
//
//                seekBar.progress = seekBarProgress.value
//
//                seekBar
//            },
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .padding(bottom = 16.dp)
//        )
//    }
//}
//
//// 下载网络视频到本地
//suspend fun downloadVideo(url: String, outputPath: String) = withContext(Dispatchers.IO) {
//    val connection = URL(url).openConnection()
//    val inputStream = connection.getInputStream()
//    val file = File(outputPath)
//    val outputStream = FileOutputStream(file)
//
//    val buffer = ByteArray(1024)
//    var bytesRead = inputStream.read(buffer)
//    while (bytesRead != -1) {
//        outputStream.write(buffer, 0, bytesRead)
//        bytesRead = inputStream.read(buffer)
//    }
//
//    outputStream.close()
//    inputStream.close()
//}
//
//@Composable
//@Preview
//fun VideoPlayerPreview(){
//    val uri = "http://82.156.30.206:8000/media/postVideo/android.mp4".toUri()
//    VideoPlayPage(uri)
//}