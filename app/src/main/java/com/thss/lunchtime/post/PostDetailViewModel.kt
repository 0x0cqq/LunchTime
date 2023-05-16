package com.thss.lunchtime.post

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thss.lunchtime.data.userPreferencesStore
import com.thss.lunchtime.network.LunchTimeApi
import com.thss.lunchtime.network.toCommentData
import com.thss.lunchtime.network.toPostData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PostDetailViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PostDetailData())
    val uiState = _uiState.asStateFlow()

    fun refresh(context: Context, postID: Int) {
        viewModelScope.launch {
            val userData = context.userPreferencesStore
            _uiState.update {
                it.copy(isRefreshing = true)
            }
            try {
                val response = LunchTimeApi.retrofitService.getPostDetail(
                    userData.data.first().userName,
                    postID
                )
                if (response.status) { // valid response
                    _uiState.update {
                        it.copy(
                            postData = response.post.toPostData(),
                            commentDataList = response.comments.map { comment ->
                                comment.toCommentData()
                            }
                        )
                    }
                } else { // invalid response
                    Toast.makeText(context, "无法获取详情, ${response.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show()
            }
            _uiState.update {
                it.copy(isRefreshing = false)
            }
        }
    }

    fun updateCurrentComment(value : String) {
        _uiState.update {
            it.copy(currentCommentInput = value)
        }
    }

    fun sendComment(context: Context) {
//        viewModelScope.launch {
//            val userData = context.userPreferencesStore
//            try {
//                val response =
//                if (response.status) { // valid response
//                    _uiState.update {
//                        it.copy(
//                            commentDataList = it.commentDataList + response.comment.toCommentData(),
//                            currentCommentInput = ""
//                        )
//                    }
//                } else { // invalid response
//                    Toast.makeText(context, "无法发送评论, ${response.message}", Toast.LENGTH_SHORT).show()
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show()
//            }
//        }
    }
}