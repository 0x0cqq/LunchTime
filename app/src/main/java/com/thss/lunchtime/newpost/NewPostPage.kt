package com.thss.lunchtime.newpost

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thss.lunchtime.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun NewPostPage(onClickBack: () -> Unit = {}, onClickSend : () -> Unit = {}, newPostViewModel: NewPostViewModel = viewModel()) {
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
                        onClick = onClickSend,
                    ) {
                        Icon(
                            Icons.Outlined.Send,
                            contentDescription = "Send",
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        NewPostContent(
            newPostViewModel = newPostViewModel,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun NewPostContent(newPostViewModel: NewPostViewModel, modifier: Modifier = Modifier) {
    val uiState = newPostViewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    Box(modifier = modifier.fillMaxSize()) {
        Column {
            TextField(
                value = uiState.value.title,
                textStyle = MaterialTheme.typography.headlineSmall,
                onValueChange = { newPostViewModel.changeTitle(it) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = {
                    Text(
                        "Title",
                        style = MaterialTheme.typography.headlineSmall,
                    )
                },
                keyboardActions = KeyboardActions {
                    focusManager.moveFocus(FocusDirection.Next)
                },
            )
            TextField(
                value = uiState.value.content,
                onValueChange = { newPostViewModel.changeContent(it) },
                modifier = Modifier.fillMaxSize(),
                placeholder = {
                    Text(
                        "Content",
                    )
                },
                keyboardActions = KeyboardActions {
                    focusManager.moveFocus(FocusDirection.Next)
                },
            )
        }
    }
}