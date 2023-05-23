package com.thss.lunchtime.listpages

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thss.lunchtime.component.InfoData
import com.thss.lunchtime.component.InfoPreviewComp
import com.thss.lunchtime.mainscreen.infopage.ImageChange
import com.thss.lunchtime.mainscreen.infopage.InfoEditPage
import com.thss.lunchtime.mainscreen.infopage.InfoEditViewModel
import com.thss.lunchtime.mainscreen.infopage.SimpleInfoChange
import com.thss.lunchtime.post.PostData
import com.thss.lunchtime.post.PostReviewCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun FansListPage(followingList: List<InfoData>) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {

    }
    Scaffold(
        topBar = {
            SmallTopAppBar(
                navigationIcon = {
                    IconButton(onClick = {  }) {
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
                items(followingList) { infoData ->
                    InfoPreviewComp(msg = infoData)
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
    FansListPage (followingList = FansArray)
}