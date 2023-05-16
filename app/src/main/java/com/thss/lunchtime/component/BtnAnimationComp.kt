package com.thss.lunchtime

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ModeComment
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thss.lunchtime.common.NoRippleInteractionSource
import com.thss.lunchtime.ui.theme.Purple40
import com.thss.lunchtime.ui.theme.Love
import com.thss.lunchtime.ui.theme.Star
import com.thss.lunchtime.ui.theme.Disabled


@Composable
fun LikeBtn(onClickLike: () -> Unit, likeCount: Int, isLiked: Boolean) {
    var change by remember{ mutableStateOf(false) }

    val buttonSize by animateDpAsState(
        targetValue = if(change) 32.dp else 24.dp
    )
    if(buttonSize == 32.dp) {
        change = false
    }

    Row (verticalAlignment = Alignment.CenterVertically,) {
        IconButton(
            onClick = {
                change = true
                onClickLike()
            },
            interactionSource = NoRippleInteractionSource()
        ) {
            Icon(
                Icons.Rounded.Favorite,
                contentDescription = "Like",
                modifier = Modifier.size(buttonSize),
                tint = if(isLiked) Love else Disabled
            )
        }

        Text(text = likeCount.toString())
    }

}


@Composable
fun StarBtn(onClickStar: () -> Unit, starCount : Int, isStared: Boolean) {
    var change by remember{ mutableStateOf(false) }

    val buttonSize by animateDpAsState(
        targetValue = if(change) 34.dp else 28.dp
    )
    if(buttonSize == 34.dp) {
        change = false
    }

    Row (verticalAlignment = Alignment.CenterVertically,) {
        IconButton(
            onClick = {
                change = true
                onClickStar()
            },
            interactionSource = NoRippleInteractionSource()
        ) {
            Icon(
                Icons.Rounded.Star,
                contentDescription = "Star",
                modifier = Modifier.size(buttonSize),
                tint = if(isStared) Star else Disabled
            )
        }

        Text(text = starCount.toString())
    }
}

@Composable
fun ThumbBtn() {
    var change by remember{ mutableStateOf(false) }
    var flag by remember{ mutableStateOf(false) }


    val buttonSize by animateDpAsState(
        targetValue = if(change) 30.dp else 24.dp
    )
    if(buttonSize == 30.dp) {
        change = false
    }

    Row (verticalAlignment = Alignment.CenterVertically,) {
        IconButton(
            onClick = {
                change = true
                flag = !flag
            },
            interactionSource = NoRippleInteractionSource()
        ) {
            Icon(
                if (flag) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                contentDescription = null,
                modifier = Modifier.size(buttonSize),
                tint = if(flag) Purple40 else Disabled
            )
        }
    }
}

@Composable
fun CommentBtn(onClickComment: () -> Unit, commentCount: Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onClickComment) {
            Icon(Icons.Filled.ModeComment, "Comment", tint = Disabled)
        }
        Text(text = commentCount.toString())
    }
}

@Preview
@Composable
fun LikeBtnAnimationPreview() {
    LikeBtn({}, 10, false)
}

@Preview
@Composable
fun StarBtnAnimationPreview() {
    StarBtn({}, 10, false)
}