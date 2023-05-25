package com.thss.lunchtime.listpages

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thss.lunchtime.data.userPreferencesStore
import com.thss.lunchtime.network.LunchTimeApi
import com.thss.lunchtime.network.Post
import com.thss.lunchtime.network.toPostData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class StarPostListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(StarPostListState())
    val uiState = _uiState.asStateFlow()

    fun refresh(context: Context, userName: String){
        viewModelScope.launch {
            val userData = context.userPreferencesStore
            try{
                val response = LunchTimeApi.retrofitService.getPostListSaved(
                    name = userData.data.first().userName,
                    targetName = userName
                )
                if (response.status){
                    _uiState.update { state ->
                        state.copy(
                            postDataList = response.posts.map { post : Post ->
                                post.toPostData()
                            }
                        )
                    }
                }
            } catch (e : IOException) {
                e.printStackTrace()
                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show()
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
                    // update likeCount and isLiked
                    val newPostDataList = uiState.value.postDataList.map{ postData ->
                        if(postData.postID == postID){
                            if(response.result == 1){
                                postData.copy(likeCount = postData.likeCount + 1, isLiked = true)
                            }
                            else{
                                postData.copy(likeCount = postData.likeCount - 1, isLiked = false)
                            }
                        }
                        else{
                            postData
                        }
                    }
                    _uiState.update{state ->
                        state.copy(postDataList = newPostDataList)
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
                    val newPostDataList = uiState.value.postDataList.map{ postData ->
                        if(postData.postID == postID){
                            if(response.result == 1){
                                postData.copy(starCount = postData.starCount + 1, isStared = true)
                            }
                            else{
                                postData.copy(starCount = postData.starCount - 1, isStared = false)
                            }
                        }
                        else{
                            postData
                        }
                    }
                    _uiState.update{state ->
                        state.copy(postDataList = newPostDataList)
                    }
                } else {
                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                }
            } catch (e : IOException) {
                e.printStackTrace()
                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show()
            }
        }
    }
}