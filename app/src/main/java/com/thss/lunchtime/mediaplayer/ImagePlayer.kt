package com.thss.lunchtime.mediaplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.core.net.toUri
import coil.compose.AsyncImage

@Composable
fun ImagePlayPage(url: String, onBack: () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onBack() },
        contentAlignment = Alignment.Center,
    ) {
        AsyncImage(
            model = url.toUri(),
            contentDescription = "image",
            contentScale = ContentScale.Crop,
            modifier = Modifier,
            alignment = Alignment.Center
        )
    }
}