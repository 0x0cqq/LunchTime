package com.thss.lunchtime

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thss.lunchtime.ui.theme.Purple40


data class Like(
    var likeCnt: Int,
    var isLike: Boolean
)

@Composable
fun LikeBtn(onClickLike: () -> Unit, like: Like) {
    var change by remember{ mutableStateOf(false) }
    var flag by remember{ mutableStateOf(like.isLike) }

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
                like.isLike = !like.isLike
                flag = !flag
                onClickLike()
            }
        ) {
            Icon(
                Icons.Rounded.Favorite,
                contentDescription = null,
                modifier = Modifier.size(buttonSize),
                tint = if(flag) Color.Red else Color.Gray
            )
        }

        Text(text = like.likeCnt.toString(), color = Color.Gray)
    }

}

data class Star(
    var starCnt: Int,
    var isStar: Boolean
)

@Composable
fun StarBtn(onClickStar: () -> Unit, star: Star) {
    var change by remember{ mutableStateOf(false) }
    var flag by remember{ mutableStateOf(star.isStar) }
//    var cnt by remember {
//        mutableStateOf(star.starCnt)
//    }

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
                star.isStar = !star.isStar
                flag = !flag
                onClickStar()
            },
        ) {
            Icon(
                Icons.Rounded.Star,
                contentDescription = null,
                modifier = Modifier.size(buttonSize),
                tint = if(flag) Color.Yellow else Color.Gray
            )
        }

        Text(text = star.starCnt.toString(), color = Color.Gray)
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
        ) {
            Icon(
                if (flag) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                contentDescription = null,
                modifier = Modifier.size(buttonSize),
                tint = if(flag) Purple40 else Color.Gray
            )
        }
    }
}

@Preview
@Composable
fun LikeBtnAnimationPreview() {
    LikeBtn({}, like = Like(10, false))
}

@Preview
@Composable
fun StarBtnAnimationPreview() {
    StarBtn({}, star = Star(10, false))
}