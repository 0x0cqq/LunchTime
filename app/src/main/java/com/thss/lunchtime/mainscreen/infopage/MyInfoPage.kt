package com.thss.lunchtime.mainscreen.infopage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thss.lunchtime.component.InfoComp
import com.thss.lunchtime.component.InfoType
import com.thss.lunchtime.post.PostData
import com.thss.lunchtime.post.PostReviewCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun MyInfoPage(onOpenInfoEdit: () -> Unit, myInfoPageViewModel: MyInfoPageViewModel) {
    val uiState = myInfoPageViewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        myInfoPageViewModel.refresh(context)
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(text = "我的主页")
                },
                actions = {
                    IconButton(onClick = { onOpenInfoEdit() }) {
                        Icon(imageVector = Icons.Default.Settings,
                            contentDescription = null)
                    }
                },
            )
        }
    ) {
        paddingValues -> Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            InfoComp(
                msg = uiState.value.infoData,
                type = InfoType.Self,
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Text(text = "我的动态", fontSize = 14.sp)

            Icon(Icons.Rounded.Sort, contentDescription = null, Modifier.size(18.dp))
        }

        LazyColumn(modifier = Modifier.fillMaxSize()
        ) {
            items(uiState.value.postList) { postData ->
                PostReviewCard({}, {}, {}, msg = postData)
            }
        }
    } 
    }
}

val postArray = listOf(
    PostData(),
    PostData(),
    PostData(),
    PostData(),
    PostData(),
    PostData(),
)

@Preview
@Composable
fun MyInfoPagePreview() {
    MyInfoPage(onOpenInfoEdit = {}, MyInfoPageViewModel())
}