package com.thss.lunchtime

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun MyInfoPage(msg: InfoData) {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(text = "我的主页")
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
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
            .verticalScroll(
                rememberScrollState()
            )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            InfoComp(msg = msg)
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
    } 
    }
}

@Preview
@Composable
fun MyInfoPagePreview() {
    MyInfoPage(msg = InfoData(InfoType = 1))
}