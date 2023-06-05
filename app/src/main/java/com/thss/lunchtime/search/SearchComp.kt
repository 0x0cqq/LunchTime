package com.thss.lunchtime.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun SearchTextField(onClickSearch: (keyword: String)-> Unit) {
    val focusManager = LocalFocusManager.current
    var searchText by remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        BasicTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(25)
                )
                .height(40.dp)
                .padding(start = 10.dp),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            singleLine = true,
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier.weight(1f)
                    ) {
                        innerTextField()
                    }
                    IconButton(
                        onClick = { onClickSearch(searchText) }
                    ) {
                        Icon(
                            Icons.Default.Search,
                            "Send",
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }, keyboardActions = KeyboardActions {
                focusManager.clearFocus()
                onClickSearch(searchText)
            }
        )
    }
}

@Composable
fun SearchPageTopBar(onClickSearch: (index: Int, keyword: String) -> Unit) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedMenuIndex by rememberSaveable { mutableStateOf(0) }
    val dropDownMenuItems = listOf("综合", "用户", "帖子", "标题", "Tag")
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize(Alignment.TopStart),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    dropDownMenuItems[selectedMenuIndex],
                )
                IconButton(onClick = { expanded = true }) {
                    if (expanded) {
                        Icon(
                            Icons.Default.ArrowLeft,
                            contentDescription = "Shrink"
                        )
                    } else {
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = "Expand More"
                        )
                    }
                }
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }) {
                dropDownMenuItems.forEachIndexed { index, name ->
                    if (name == "Divider") {
                        Divider()
                    } else {
                        DropdownMenuItem(
                            text = { Text(name) },
                            onClick = { /* TODO */ selectedMenuIndex = index; expanded = false },
                            modifier = Modifier.wrapContentHeight(),
                        )
                    }
                }
            }
        }
        SearchTextField( onClickSearch = { text -> onClickSearch(selectedMenuIndex, text)} )
    }
}

@Preview
@Composable
fun SearchPreview(){
    SearchPageTopBar({index: Int, text:String -> })
}