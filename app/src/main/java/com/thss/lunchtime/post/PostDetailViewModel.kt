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
import java.io.IOException

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


    fun onClickLike(context: Context, postID: Int){
        viewModelScope.launch {
            val userData = context.userPreferencesStore
            try{
                val response = LunchTimeApi.retrofitService.likePost(
                    userData.data.first().userName,
                    postID)
                if (response.status){
                    // update starCount and isStared
                    _uiState.update{state ->
                        val newPostData  = if(state.postData.postID == postID){
                            if(response.result == 1){
                                state.postData.copy(likeCount = state.postData.likeCount + 1, isLiked = true)
                            }
                            else{
                                state.postData.copy(likeCount = state.postData.likeCount - 1, isLiked = false)
                            }
                        }
                        else{
                            state.postData
                        }
                        state.copy(postData = newPostData)
                    }
                } else {
                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                }
            }catch (e : IOException) {
                e.printStackTrace()
                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onClickStar(context: Context, postID: Int){
        viewModelScope.launch {
            val userData = context.userPreferencesStore
            try{
                val response = LunchTimeApi.retrofitService.starPost(
                    userData.data.first().userName,
                    postID)
                if (response.status){
                    // update starCount and isStared
                    _uiState.update{state ->
                        val newPostData  = if(state.postData.postID == postID){
                            if(response.result == 1){
                                state.postData.copy(starCount = state.postData.starCount + 1, isStared = true)
                            }
                            else{
                                state.postData.copy(starCount = state.postData.starCount - 1, isStared = false)
                            }
                        }
                        else{
                            state.postData
                        }
                        state.copy(postData = newPostData)
                    }
                } else {
                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                }
            }catch (e : IOException) {
                e.printStackTrace()
                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun updateCurrentComment(value : String) {
        _uiState.update {
            it.copy(currentCommentInput = value)
        }
    }

    fun sendComment(context: Context) {
        if(uiState.value.currentCommentInput.isEmpty()){
            Toast.makeText(context, "评论不能为空", Toast.LENGTH_SHORT).show()
            return
        }
        viewModelScope.launch {
            val userData = context.userPreferencesStore
            try {
                val response = LunchTimeApi.retrofitService.commentPost(
                    userData.data.first().userName,
                    uiState.value.postData.postID,
                    uiState.value.currentCommentInput
                )

                if (response.status) { // valid response
                    Toast.makeText(context, "评论成功!", Toast.LENGTH_SHORT).show()
                    updateCurrentComment("")
                } else { // invalid response
                    Toast.makeText(context, "无法发送评论, ${response.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show()
            }
        }
    }
}