package com.thss.lunchtime.listpages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thss.lunchtime.component.InfoData
import com.thss.lunchtime.component.InfoPreviewComp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun FansListPage(onBack: ()->Unit, onClickUserInfo:(userName: String) -> Unit, userName: String, listPageViewModel: ListPageViewModel = viewModel()) {
    val context = LocalContext.current
    val uiState = listPageViewModel.uiState.collectAsState()
    val type = 1

    LaunchedEffect(Unit) {
        listPageViewModel.refresh(context, userName, type)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                },
                title = {
                    Text(text = "粉丝列表")
                }
            )
        }
    ) { paddingValues -> Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.value.userList) { infoData ->
                    InfoPreviewComp(
                        msg = infoData,
                        modifier = Modifier.clickable { onClickUserInfo(infoData.ID) }
                    )
                }
            }
        }
    }
    }
}

val FansArray = listOf(
    InfoData(relation = 2),
    InfoData(relation = 1),
    InfoData(relation = 2),
    InfoData(relation = 1),
    InfoData(relation = 1),
)

@Preview
@Composable
fun FansPreview() {
    FansListPage ({}, {}, "")
}