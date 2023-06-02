package com.thss.lunchtime.newpost

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FormatColorText
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.outlined.FormatBold
import androidx.compose.material.icons.outlined.FormatItalic
import androidx.compose.material.icons.outlined.FormatUnderlined
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import me.onebone.parvenu.ParvenuEditor
import me.onebone.parvenu.ParvenuSpanPicker
import me.onebone.parvenu.ParvenuSpanToggle



@Composable
fun RichTextColorPalette(newPostViewModel: NewPostViewModel) {
    val uiState = newPostViewModel.uiState.collectAsState()
    val isColorPopupEnabled = remember { mutableStateOf(false) }

    val colorList = listOf(
        Color.Red, Color.Yellow, Color.Green,
        Color.Blue, Color.Cyan
    )
    ParvenuSpanPicker(
        value = uiState.value.richContent,
        onValueChange = { newPostViewModel.setRichContent(it) },
        spanStyleList = colorList.map { SpanStyle(color = it) },
        spanEqualPredicate = { firstSpan, secondSpan ->
            firstSpan.color == secondSpan.color
        }
    ) { currentSelectedIndex, onToggle ->
        IconButton(
            colors = IconButtonDefaults.filledTonalIconButtonColors(),
            onClick = { isColorPopupEnabled.value = true },
        ) {
            Icon(
                imageVector = Icons.Default.FormatColorText,
                contentDescription = "Color",
                tint = if (currentSelectedIndex == -1) Color.Unspecified else colorList[currentSelectedIndex]
            )

            if (isColorPopupEnabled.value) {
                Popup(
                    alignment = Alignment.TopCenter,
                    offset = IntOffset(0, 120),
                    onDismissRequest = { isColorPopupEnabled.value = false },
                    properties = PopupProperties(
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true,
                    )
                ) {
                    Card {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            colorList.forEachIndexed { index, color ->
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(color)
                                        .clickable { onToggle(index) }
                                ) {
                                    if (index == currentSelectedIndex) {
                                        Icon(
                                            Icons.Default.Check,
                                            "selected",
                                            tint = if (color.luminance() > 0.5) {
                                                Color.Black
                                            } else {
                                                Color.White
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RichTextFontSizePalette(newPostViewModel: NewPostViewModel) {
    val uiState = newPostViewModel.uiState.collectAsState()
    val isFontSizePopupEnabled = remember { mutableStateOf(false) }

    val fontSizeList = listOf(
        MaterialTheme.typography.headlineMedium.fontSize,
        MaterialTheme.typography.titleLarge.fontSize,
        MaterialTheme.typography.bodyMedium.fontSize,
        MaterialTheme.typography.labelMedium.fontSize
    )
    ParvenuSpanPicker(
        value = uiState.value.richContent,
        onValueChange = { newPostViewModel.setRichContent(it) },
        spanStyleList = fontSizeList.map { SpanStyle(fontSize = it) },
        spanEqualPredicate = { firstSpan, secondSpan ->
            firstSpan.fontSize == secondSpan.fontSize
        }
    ) { currentSelectedIndex, onToggle ->
        Button(
            colors = ButtonDefaults.filledTonalButtonColors(),
            shape = RectangleShape,
            onClick = { isFontSizePopupEnabled.value = true },
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ){
                Icon(
                    imageVector = Icons.Default.FormatSize,
                    contentDescription = "Size",
                )
                if(currentSelectedIndex != -1) {
                    Text(
                        "${fontSizeList[currentSelectedIndex].value.toInt()}",
                        fontSize = 18.sp
                    )
                }
            }

            if (isFontSizePopupEnabled.value) {
                Popup(
                    alignment = Alignment.TopCenter,
                    offset = IntOffset(0, 120),
                    onDismissRequest = { isFontSizePopupEnabled.value = false },
                    properties = PopupProperties(
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true,
                    )
                ) {
                    Card {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            fontSizeList.forEachIndexed { index, fontSize ->
                                Box(
                                    modifier = Modifier
                                        .background(
                                            if (index == currentSelectedIndex) {
                                                MaterialTheme.colorScheme.primaryContainer
                                            } else {
                                                Color.Unspecified
                                            }
                                        )
                                        .clickable{ onToggle(index) }
                                        .padding(vertical = 10.dp, horizontal = 12.dp)

                                ) {
                                    Text(
                                        "${fontSize.value.toInt()}",
                                        color = if (index == currentSelectedIndex) {
                                            MaterialTheme.colorScheme.onPrimaryContainer
                                        } else {
                                            Color.Unspecified
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPostTitleContent(newPostViewModel: NewPostViewModel, focusManager: FocusManager) {
    val isFocusedOnContent = remember { mutableStateOf(false) }
    val uiState = newPostViewModel.uiState.collectAsState()

    Column {
        TextField(
            value = uiState.value.title,
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            onValueChange = { newPostViewModel.setTitle(it) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = {
                // default color, but with higher alpha
                Text(
                    "输入标题",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            },
            keyboardActions = KeyboardActions { focusManager.moveFocus(FocusDirection.Next) },
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
            ),
        )
        // rich text bar
        if (isFocusedOnContent.value) {
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                item {
                    ParvenuSpanToggle(
                        value = uiState.value.richContent,
                        onValueChange = { newPostViewModel.setRichContent(it) },
                        spanFactory = { SpanStyle(fontWeight = FontWeight.Bold) },
                        spanEqualPredicate = { style ->
                            style.fontWeight == FontWeight.Bold
                        }
                    ) { enabled, onToggle ->
                        IconToggleButton(
                            colors = IconButtonDefaults.filledIconToggleButtonColors(),
                            checked = enabled,
                            onCheckedChange = { onToggle() }
                        ) {
                            Icon(Icons.Outlined.FormatBold, "Bold")
                        }
                    }
                }
                item {
                    ParvenuSpanToggle(
                        value = uiState.value.richContent,
                        onValueChange = { newPostViewModel.setRichContent(it) },
                        spanFactory = { SpanStyle(fontStyle = FontStyle.Italic) },
                        spanEqualPredicate = { style ->
                            style.fontStyle == FontStyle.Italic
                        }
                    ) { enabled, onToggle ->
                        IconToggleButton(
                            colors = IconButtonDefaults.filledIconToggleButtonColors(),
                            checked = enabled,
                            onCheckedChange = { onToggle() }
                        ) {
                            Icon(Icons.Outlined.FormatItalic, "Italic")
                        }
                    }
                }
                item {
                    ParvenuSpanToggle(
                        value = uiState.value.richContent,
                        onValueChange = { newPostViewModel.setRichContent(it) },
                        spanFactory = { SpanStyle(textDecoration = TextDecoration.Underline) },
                        spanEqualPredicate = { style ->
                            style.textDecoration == TextDecoration.Underline
                        }
                    ) { enabled, onToggle ->
                        IconToggleButton(
                            colors = IconButtonDefaults.filledIconToggleButtonColors(),
                            checked = enabled,
                            onCheckedChange = { onToggle() }
                        ) {
                            Icon(Icons.Outlined.FormatUnderlined, "Underlined")
                        }
                    }
                }
                item {
                    RichTextColorPalette(newPostViewModel)
                }
                item {
                    RichTextFontSizePalette(newPostViewModel)
                }
            }
        }
        // rich text editor
        ParvenuEditor(
            value = uiState.value.richContent,
            onValueChange = { newPostViewModel.setRichContent(it) },
        ) { value, onValueChange ->
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth()
                    .onFocusChanged { isFocusedOnContent.value = it.hasFocus },
                placeholder = {
                    Text(
                        "内容...",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                },
                keyboardActions = KeyboardActions { focusManager.moveFocus(FocusDirection.Next) },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                ),
            )
        }

    }
}